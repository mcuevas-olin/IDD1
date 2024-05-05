# K-means Clustering Algorithm Understanding

## Overview:

The K-means clustering algorithm is a popular unsupervised learning technique used for partitioning a dataset into a predetermined number of clusters. Each cluster is represented by its centroid, which is the mean of all the data points belonging to that cluster. The algorithm iteratively assigns data points to the nearest centroids and then updates the centroids based on the assigned data points. This process continues until convergence, where the centroids no longer change significantly or a predefined number of iterations is reached.

### Data Representation:

The algorithm operates on a dataset comprising data points in an i-dimensional space. Each data point is represented by a vector containing its coordinates in the i-dimensional space. In the provided Kotlin implementation, the `DataPoint` class encapsulates these coordinates.

```kotlin
// Data point class representing a point in the i-dimensional space
data class DataPoint(val coordinates: MutableList<Double>)
```

## Initialization:

The K-means algorithm begins by randomly initializing K centroids, where K is the number of clusters desired. In the `initializeCentroids()` function, K data points are randomly selected from the input dataset to serve as the initial centroids.

```kotlin
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
```

## Assignment Step:

Once the centroids are initialized, the algorithm iteratively assigns each data point to the nearest centroid based on Euclidean distance. In the `assignmentStep()` function, each data point is compared with all centroids, and it is assigned to the centroid with the minimum Euclidean distance.

```kotlin
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
```

## Update Step:

After the assignment step, the centroids are updated based on the current assignments of data points. In the `updateStep()` function, the centroids' positions are recalculated by computing the weighted mean of the data points assigned to each centroid. The weights are determined by the responsibilities of the data points.

```kotlin
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
```

## Soft K-means Extension:

The provided implementation also includes an extension for Soft K-means clustering, which assigns probabilities to data points for each centroid based on exponential similarities. The degree of allowed overlap is defined by the stiffness parameter aptly named stiffness. This extension allows for softer assignment of data points to centroids, useful in scenarios where data points may belong to multiple clusters simultaneously. However, if the clusters are clearly defined, and each data point can only belong to one cluster, then it would be best to go with the classic k-means algorithm.

### Soft Assignment Step:

The soft assignment step in the `assignmentStepSoft()` function calculates exponential similarities between data points and centroids, assigning probabilities to each data point for each centroid based on these similarities.\
``` kotlin
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
```


### Soft Update Step:

The update step for soft K-means, is the same as the basic K-means, updates centroids based on the current assignments of data points. However, instead of the assignment being 1 to the corresponding centroid, it considers the probabilities of data points belonging to centroids, adjusting the centroid positions accordingly.

```
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
        }

        // Update centroid
        if (totalR > 0) {
            for (d in totalMeans.coordinates.indices) {
                totalMeans.coordinates[d] = totalMeans.coordinates[d] / totalR
            }
            centroids[j] = totalMeans
        }
    }
}
```
# Real-world Applications Summary
 
## Strengths and Weaknesses of K-means Clustering:
### Strengths:
- Simplicity and efficiency make it suitable for large datasets.
- Versatility allows application in various domains such as customer segmentation, image segmentation, and anomaly detection.
### Weaknesses:
- Sensitivity to initial centroid selection may lead to suboptimal results.
- Assumes clusters are spherical and of equal size, limiting performance for non-linear or irregularly shaped clusters.

## Real-world Use Cases:
- **Marketing**: Segmentation of customers based on purchasing behavior for targeted marketing.
- **Image Processing**: It is often times used as a starting base for image compression and segmentation by grouping similar pixels.
- **Healthcare**: Disease diagnosis and patient profiling by clustering individuals with similar medical histories or symptoms.

## Other Commonly Used Clustering Algorithms:
- **Hierarchical Clustering (e.g., Agglomerative Clustering)**:
  - Offers flexibility in identifying clusters of varying shapes and sizes.
- **Density-based Clustering (e.g., DBSCAN)**:
  - Effective in detecting clusters of arbitrary shapes and handling noise in data.
- **Spectral Clustering**:
  - Preferred for data with complex structures or unknown number of clusters, capturing intricate relationships between data points.

 Understanding the strengths, weaknesses, and alternative clustering algorithms helps in selecting the most appropriate method for specific applications.


# Project Proposal: K-means Clustering Exploration

## Objective:
The objective of this project is to delve into the intricacies of the K-means clustering algorithm, implement it in Kotlin, and explore various aspects related to its functionality, optimization, and real-world applications.

## Scope:
The project will cover the following key areas of investigation:

1. **Basic K-means Implementation**:
    - Implement the K-means clustering algorithm in Kotlin, ensuring clarity and correctness of the code.

2. **Soft K-means Implementation**:
    - Implement Soft K-means clustering, which assigns probabilities to data points for each centroid.

3. **Summary and Description of Algorithm**:
    - Provide a comprehensive understanding of the K-means and Soft K-means clustering algorithm, including its initialization, assignment, and update steps.

4. **Real-world Applications Analysis**:
    - Research and analyze real-world applications of K-means clustering across various domains.
    - Investigate how K-means clustering has been applied in past and current scenarios, identifying its strengths, weaknesses, and potential areas for improvement.

## Deliverables:

1. **Basic K-means Implementation**:
    - A well-documented Kotlin code implementing the K-means clustering algorithm, demonstrating understanding of the algorithm.

2. **Soft K-means Implementation**:
    - A well-documented Kotlin code implementing Soft K-means clustering algorithm, demonstrating understanding of the algorithm.

3. **Algorithm Understanding Summary**:
    - A detailed summary explaining the workings of the K-means and Soft K-means clustering algorithm, covering its key steps and concepts.

4. **Real-world Applications Summary**:
    - A brief summary detailing the real-world applications of K-means clustering, including its strengths, weaknesses, and common use cases. If K-means is not commonly used, provide insights into more commonly used clustering algorithms and their advantages.

5. **Unit Tests**:
    - Unit tests for the implementations to ensure functionality and reliability.

## Time Estimate:

- Algorithm Understanding and Implementation: 10 hours
- Real-world Applications Analysis: 1 hour
- Documentation and Report Writing: 4 hours

## Grading Rubric:

- **Basic K-means Implementation (3 points)**:
    - Functional and easy-to-understand code.
    - Good comments and function documentation.
    - Functional unit tests.

- **Soft K-means Implementation (3 points)**:
    - Functional and easy-to-understand code.
    - Good comments and function documentation.
    - Functional unit tests.

- **Algorithm Understanding (2 points each for Basic and Soft K-means)**:
    - Clear and concise summary of the algorithm.
    - Explanation of when to use Basic K-means versus Soft K-means.

- **Real-world Applications Analysis (3 points)**:
    - States general strengths and weaknesses of K-means clustering.
    - Discusses real-world use cases of K-means clustering.
    - Provides insights into more commonly used clustering algorithms and their advantages.

**Total Points: 13 points**




