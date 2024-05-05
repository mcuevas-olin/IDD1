package org.example

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.exp
import kotlin.random.Random

// Data point class representing a point in the i-dimensional space
data class DataPoint(val coordinates: MutableList<Double>)

class KmeansClustering(private val k: Int = 2, private val data: List<DataPoint>) {

    private var centroids = mutableListOf<DataPoint>()


    /**
     * Runs K means clustering algorithm until either the means converge or it hits max iterations.
     * @param maxIteration Maximum number of iterations to run. If 0, it will run until convergence.
     * @return The final assignment matrix after clustering.
     */
    fun runKmeans(maxIteration: Int = 400): MutableList<MutableList<Boolean>> {
        initializeCentroids()
        val r  = initializeResponsibilities()

        var iteration = 0
        var assignmentsChanged = true

        // Repeat assignment and update steps until assignments do not change or reach max iterations
        while (assignmentsChanged && (iteration < maxIteration || maxIteration == 0)) {
            val previousAssignments = r.map { it.toList() } // Copy current assignments
            //cluster data to centroid
            assignmentStep()

            //update centroid position
            updateStep<Boolean>(r)
            assignmentsChanged = !assignmentsConverged<Boolean>(previousAssignments,r)
            iteration++
        }

        return r
    }

    /**
     * Runs Soft K means clustering algorithm until either the means converge or it hits max iterations.
     * @param maxIteration Maximum number of iterations to run. If 0, it will run until convergence.
     * @param stiffness Determines how "Hard" or "Soft" the clustering is
     * @return The final assignment matrix after clustering.
     */
    fun runKmeansSoft(maxIteration: Int = 400, stiffness : Double = 0.5): MutableList<MutableList<Double>> {

        initializeCentroids()
        val r  = initializeResponsibilitiesSoft()

        var iteration = 0
        var assignmentsChanged = true

        // Repeat assignment and update steps until assignments do not change or reach max iterations
        while (assignmentsChanged && (iteration < maxIteration || maxIteration == 0)) {
            val previousAssignments = r.map { it.toList() } // Copy current assignments
            //cluster data to centroid
            assignmentStep()

            //update centroid position
            updateStep<Double>(r)
            assignmentsChanged = !assignmentsConverged(previousAssignments, r)
            iteration++
        }

        //find which centroid is closer

        return r

    }

    /**
     * Assigns each data point to the nearest centroid based on Euclidean distance.
     * Updates the responsibility matrix accordingly for hard clustering.
     */
    private fun assignmentStep() {
        // Reset all current responsibilities to false.
        val r = initializeResponsibilities()

        // Iterate through data
        for (i in data.indices) {
            // Find the closest centroid
            var currentMinDistance = Double.MAX_VALUE
            var currentMinCentroidIndex = -1

            // Iterate through centroids
            for (j in centroids.indices) {
                // Calculate distance
                val distance = euclideanDistance(data[i], centroids[j])

                // Update closest centroid if necessary
                if (distance < currentMinDistance) {
                    currentMinCentroidIndex = j
                    currentMinDistance = distance
                }
            }

            // Update responsibility
            r[i][currentMinCentroidIndex] = true
        }
    }

    /**
     * Assigns each data point to centroids based on exponential similarities.
     * Updates the responsibility matrix accordingly for soft clustering.
     * @param stiffness Determines the 'softness' of the clustering, affecting the influence of distances on responsibilities.
     * @return The updated responsibility matrix reflecting the probabilities of data points belonging to centroids.
     */
    private fun assignmentStepSoft(stiffness: Double): MutableList<MutableList<Double>> {
        // Reset all current responsibilities to false.
        val r = initializeResponsibilitiesSoft()

        //Iterate through data
        for (i in data.indices){
            var sumExpSimilarities :Double = 0.0
            //Iterate through centroids to find the exponential similarities of data point to all clusters
            for (j in centroids.indices){
                //Calculate exponential similarity
                r[i][j] = exp(-stiffness * euclideanDistance(data[i], centroids[j]))

                //Calculate the total sum of the exp similarities of all clusters in relation to the current data point
                sumExpSimilarities += r[i][j]
            }
            //calculate the responsibilities by dividing the exponential similarity for the specific cluster over the exponential similarity for all clusters
            for (j in centroids.indices){
                r[i][j] = r[i][j]/sumExpSimilarities
            }
        }
        return r
    }

    /**
     * Updates the centroids based on the current assignments and data points.
     * Calculates the new centroid positions by computing the weighted mean of data points assigned to each centroid.
     * The weights are determined by the responsibilities of the data points.
     * @param r The responsibility matrix indicating the assignments of data points to centroids.
     */
    private fun <T : Any>updateStep(r: MutableList<MutableList<T>>) {

        for (j in centroids.indices) {
            var totalR: Double = 0.0
            var totalMeans = DataPoint(MutableList<Double>(data[0].coordinates.size) { 0.0 })

            // Calculate total responsibility and sum of coordinates
            for (i in data.indices) {
                totalR += toDouble(r[i][j])

                for (d in data[i].coordinates.indices) {
                    totalMeans.coordinates[d] += toDouble(r[i][j])*data[i].coordinates[d]
                }
                /*if (r[i][j]) {
                    totalR++
                    for (d in data[i].coordinates.indices) {
                        totalMeans.coordinates[d] += data[i].coordinates[d]
                    }

                 */
                }


            // Update centroid
            if (totalR > 0) {
                for (d in totalMeans.coordinates.indices) {
                    totalMeans.coordinates[d] =   totalMeans.coordinates[d]/totalR
                }
                centroids[j] = totalMeans
            }
        }
    }



    /**
     * Function to calculate Euclidean distance between two i-dimensional data points
     * @param point1 DataPoint one point you are finding the distance between
     * @param point2 DataPoint the other point you are finding the distance between
     * @return Euclidean distance between the two points
     *
     * It does not matter which is point1 and which is point2, the end result will be the same.
     * Make sure that point1 and point2 have the same size. This ensures that they have the same dimensionality.
     */
    private fun euclideanDistance(point1: DataPoint, point2: DataPoint): Double {
        var sum = 0.0
        require(point1.coordinates.size == point2.coordinates.size) { "Dimensions of points must be equal" }
        // Find the square of the difference between the two points in each dimension
        for (i in point1.coordinates.indices) {
            sum += (point1.coordinates[i] - point2.coordinates[i]).pow(2)
        }
        // Return the square root of the sum of squares of differences
        return sqrt(sum)
    }

    /**
     * Function to initialize k centroids by randomly choosing k DataPoints from data
     * Selects k random data points from the input data to use as initial centroids.
     */
    private fun initializeCentroids() {
        val randomIndices = mutableListOf<Int>()
        // Until k centroids have been chosen
        while (randomIndices.size < k) {
            // Select a random integer from 0 (inclusive) to the size of the data (exclusive)
            val index = Random.nextInt(0, data.size)
            // Choose the corresponding data point at that index if it has not already been chosen
            if (!randomIndices.contains(index)) {
                randomIndices.add(index)
                centroids.add(data[index])
            }
        }
    }


    /**
     * Initializes the responsibility matrix for hard clustering.
     * Creates a boolean matrix where each row corresponds to a data point and each column corresponds to a centroid.
     * The value at [i][ j] indicates whether data point i is assigned to centroid j.
     * @return The initialized responsibility matrix.
     */
    private fun initializeResponsibilities(): MutableList<MutableList<Boolean>> {
       val r = MutableList(data.size){MutableList(k){false}}
        return r
    }


    /**
     * Initializes the responsibility matrix for soft clustering.
     * Creates a matrix of doubles where each row corresponds to a data point and each column corresponds to a centroid.
     * The value at [i][ j] indicates the responsibility of data point i for centroid j, represented as a probability.
     * @return The initialized responsibility matrix.
     */
    private fun initializeResponsibilitiesSoft(): MutableList<MutableList<Double>> {
        val r = MutableList(data.size) { MutableList(k) { 0.0 } }
        return r
    }

    /**
     * Checks whether the current assignments have converged.
     * Convergence occurs when the current assignments are the same as the previous assignments.
     * @param previousAssignments The assignments from the previous iteration.
     * @return true if assignments have converged, false otherwise.
     */
    private fun <T>assignmentsConverged(previousAssignments: List<List<T>>, r : MutableList<MutableList<T>>): Boolean {
    // Check if the current assignments are the same as the previous assignments
        for (i in r.indices) {
            for (j in r[i].indices) {
                // If any assignment differs from the previous one, return false indicating assignments have not converged
                if (r[i][j] != previousAssignments[i][j]) {
                    return false
                }
            }
        }
        // If all assignments are the same as the previous ones, return true indicating assignments have converged
        return true
    }

    /**
     * Converts a Boolean or a Number to a Double.
     * @param value The value to convert to Double.
     * @return The converted value as a Double.
     * @throws IllegalArgumentException if the type of value is not supported.
     */
    fun <T> toDouble(value: T): Double {
        return when (value) {
            is Boolean -> if (value) 1.0 else 0.0
            is Number -> value.toDouble()
            else -> throw IllegalArgumentException("Unsupported type: ${value?.let { it::class.simpleName }}")
        }
    }




}
