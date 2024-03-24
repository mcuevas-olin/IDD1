package org.example

import javax.xml.crypto.Data
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

// Data point class representing a point in the i-dimensional space
data class DataPoint(val coordinates: MutableList<Double>)
class KmeansClustering(k: Int, data: List<DataPoint>){
    private var centroids = mutableListOf<DataPoint>()
    private var r = MutableList(data.size) { MutableList(k) { false } }


    init {
        // Initialize centroids upon object creation
        initializeCentroids()

    }

    private fun assignmentStep(){
        //reset all current responsibilities to 0.
        initializeResponsibilities()

        //iterate through data
        for (currentDatapoint in self.data){
            //find the closest centroid

            //initialize the min distance as the maximum possible value
            var currentMinDistance = Double.MAX_VALUE
            var currentMinCentroid = 0;

            //iterate through centroids
            for (currentCentroidIndex in centroids.indices){
                //calculate distance
                val distance = euclideanDistance(currentDatapoint, centroids[currentCentroidIndex])

                //save info if min
                if (distance < currentMinDistance){
                    currentMinCentroid = currentCentroidIndex
                    currentMinDistance = distance
                }
            }

            //save corresponding closest centroid as true
            r[currentDatapoint][currentMinCentroid] = true

        }
    }

    private fun updateStep(){
        for (currentCentroidIndex in centroids.indices) {
            var totalR = 0
            var totalmeans = DataPoint(MutableList<Double>(data.size){0.0});
            for (currentDataIndex in self.data.index) {
                if (r[currentDataIndex][currentCentroidIndex]){
                    totalR ++
                    totalmeans += currentDataIndex

                }
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
        //find the square of the difference between the two points in each dimension
        for (i in point1.coordinates.indices) {
            sum += (point1.coordinates[i] - point2.coordinates[i]).pow(2)
        }
        //return the square root of the square of the difference between the two points in each dimension
        return sqrt(sum)
    }

     /**
      * Function to initialize k centroids by randomly choosing k DataPoints from data
      */
    private fun initializeCentroids(){
        val randomIndices = mutableListOf<Int>()
         //Until k centroids have been chosen

         while (randomIndices.size < self.k) {
            //select a random integer from 0 (inclusive)to the size of the data(exclusive)
            val index = Random.nextInt(0, self.data.size)

            //choose the corresponding datapoint at that index if it has not already been chosen
            if (!randomIndices.contains(index)) {
                randomIndices.add(index)
                centroids.add(self.data[index])
            }
        }
    }

    private fun initializeResponsibilities(){
        r = MutableList(data.size) { MutableList(k) { false } }
    }
}

