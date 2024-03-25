package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
        // Sample data
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

        // Print the assignments
        println("Final Assignments:")
        for ((index, assignment) in assignments.withIndex()) {
            println("Data point $index -> Cluster: ${assignment.indexOf(true)}")
        }

        // Expected result:
        // Final Assignments:
        // Data point 0 -> Cluster: 0
        // Data point 1 -> Cluster: 0
        // Data point 2 -> Cluster: 0
        // Data point 3 -> Cluster: 1
        // Data point 4 -> Cluster: 1
        // Data point 5 -> Cluster: 1
    }