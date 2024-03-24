<script
  src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
  type="text/javascript">
</script>

# K-means clustering 
This is an algorithm to divide data points into clusters characterized by its mean, a vector $$ m k $$.
The data points are $$x^(n) $$ where the superscript denoting which data point is, from 1 to N.
Each vector x has I components, denoted as $$x_i$$ corresponding to its position in an i-dimensional space.

Assuming that x is all in a real space, we then use a metric to define distance between the points. For this
purpose, we are using the Euchidean distance.

### How it works

We first initialize K means.

Then we begin iterating through two steps.
 
Assignment Step:
Each data point is assigned to the nearest mean.

Update Step:
Each cluster calculates its new mean.




