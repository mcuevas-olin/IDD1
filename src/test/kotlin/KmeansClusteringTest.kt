package org.example

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KmeansClusteringTest {

    @Test
    fun testClusterAssignmentForTwoDimensions() {
        // Sample data with two-dimensional coordinates
        val data = listOf(
            DataPoint(mutableListOf(1.0, 2.0)),
            DataPoint(mutableListOf(2.0, 3.0)),
            DataPoint(mutableListOf(3.0, 4.0)),
            DataPoint(mutableListOf(8.0, 7.0)),
            DataPoint(mutableListOf(9.0, 8.0)),
            DataPoint(mutableListOf(10.0, 9.0))
        )

        // Number of clusters
        val k = 2

        // Initialize KmeansClustering object
        val kmeans = KmeansClustering(k, data)

        // Run K-means clustering algorithm
        val assignments = kmeans.runKmeans()

        // Check if the first three elements are assigned to the same cluster
        assertTrue(assignments[0] == assignments[1] && assignments[1] == assignments[2], "First three elements are in the same cluster")

        // Check if the last three elements are assigned to the same cluster
        assertTrue(assignments[3] == assignments[4] && assignments[4] == assignments[5], "Last three elements are in the same cluster")
    }

    @Test
    fun testSoftClusterAssignmentForTwoDimensions() {
        // Sample data with two-dimensional coordinates
        val data = listOf(
            DataPoint(mutableListOf(1.0, 2.0)),
            DataPoint(mutableListOf(2.0, 3.0)),
            DataPoint(mutableListOf(3.0, 4.0)),
            DataPoint(mutableListOf(8.0, 7.0)),
            DataPoint(mutableListOf(9.0, 8.0)),
            DataPoint(mutableListOf(10.0, 9.0))
        )

        // Number of clusters
        val k = 2

        // Initialize SoftKmeansClustering object
        val softKmeans = KmeansClustering(k, data)

        // Run Soft K-means clustering algorithm
        val assignments = softKmeans.runKmeansSoft()

        // Check if the first three elements are assigned to the same cluster
        assertTrue(assignments[0] == assignments[1] && assignments[1] == assignments[2], "First three elements are in the same cluster")

        // Check if the last three elements are assigned to the same cluster
        assertTrue(assignments[3] == assignments[4] && assignments[4] == assignments[5], "Last three elements are in the same cluster")
    }

    @Test
    fun testClusterAssignmentForThreeDimensions() {
        // Sample data with three-dimensional coordinates
        val data = listOf(
            DataPoint(mutableListOf(1.0, 2.0, 3.0)),
            DataPoint(mutableListOf(2.0, 3.0, 4.0)),
            DataPoint(mutableListOf(3.0, 4.0, 5.0)),
            DataPoint(mutableListOf(8.0, 7.0, 6.0)),
            DataPoint(mutableListOf(9.0, 8.0, 7.0)),
            DataPoint(mutableListOf(10.0, 9.0, 8.0))
        )

        // Number of clusters
        val k = 2

        // Initialize KmeansClustering object
        val kmeans = KmeansClustering(k, data)

        // Run K-means clustering algorithm
        val assignments = kmeans.runKmeans()

        // Check if the first three elements are assigned to the same cluster
        assertTrue(assignments[0] == assignments[1] && assignments[1] == assignments[2], "First three elements are in the same cluster")

        // Check if the last three elements are assigned to the same cluster
        assertTrue(assignments[3] == assignments[4] && assignments[4] == assignments[5], "Last three elements are in the same cluster")
    }

    @Test
    fun testSoftClusterAssignmentForThreeDimensions() {
        // Sample data with three-dimensional coordinates
        val data = listOf(
            DataPoint(mutableListOf(1.0, 2.0, 3.0)),
            DataPoint(mutableListOf(2.0, 3.0, 4.0)),
            DataPoint(mutableListOf(3.0, 4.0, 5.0)),
            DataPoint(mutableListOf(8.0, 7.0, 6.0)),
            DataPoint(mutableListOf(9.0, 8.0, 7.0)),
            DataPoint(mutableListOf(10.0, 9.0, 8.0))
        )

        // Number of clusters
        val k = 2

        // Initialize SoftKmeansClustering object
        val softKmeans = KmeansClustering(k, data)

        // Run Soft K-means clustering algorithm
        val assignments = softKmeans.runKmeansSoft()

        // Check if the first three elements are assigned to the same cluster
        assertTrue(assignments[0] == assignments[1] && assignments[1] == assignments[2], "First three elements are in the same cluster")

        // Check if the last three elements are assigned to the same cluster
        assertTrue(assignments[3] == assignments[4] && assignments[4] == assignments[5], "Last three elements are in the same cluster")
    }
}
