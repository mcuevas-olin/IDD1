package org.example

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

// Data point class representing a point in the i-dimensional space
data class DataPoint(val coordinates: MutableList<Double>)

class KmeansClustering(private val k: Int, private val data: List<DataPoint>) {

    private var centroids = mutableListOf<DataPoint>()
    private var r = MutableList(data.size) { MutableList(k) { false } }

    /*
    * runs K means clustering algorithm until either the means converge or it hits max iterations
    * @param maxIteration
    * */
    fun runKmeans(maxIteration: Int = 0): MutableList<MutableList<Boolean>> {
        initializeCentroids()
        initializeResponsibilities()

        //loop assignmentStep and updateStep until means converge or it hits max iterations here

        return r

    }


    private fun assignmentStep() {
        // Reset all current responsibilities to false.
        initializeResponsibilities()

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

    private fun updateStep() {
        for (j in centroids.indices) {
            var totalR = 0
            val totalMeans = DataPoint(MutableList<Double>(data[0].coordinates.size) { 0.0 })

            // Calculate total responsibility and sum of coordinates
            for (i in data.indices) {
                if (r[i][j]) {
                    totalR++
                    for (d in data[i].coordinates.indices) {
                        totalMeans.coordinates[d] += data[i].coordinates[d]
                    }
                }
            }

            // Update centroid
            if (totalR > 0) {
                for (d in totalMeans.coordinates.indices) {
                    totalMeans.coordinates[d] /= totalR.toDouble()
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


    private fun initializeResponsibilities() {
        r = MutableList(data.size) { MutableList(k) { false } }
    }
}
