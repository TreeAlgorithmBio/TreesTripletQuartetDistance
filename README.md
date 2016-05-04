# TreesTripletQuartetDistance

Phylogeny is the study of the evolutionary relationships between a set of organisms. Phylogenetic trees, are a graphical representation of these relationships.

We have partially implemented the algorithm for:

Brodal GS, et al ACM-SIAM Symposium on Discrete Algorithms (SODA)New
Orleans, Louisiana, USA. Philadelphia, USA: Society for Industrial and Applied
Mathematics (SIAM); 2013. “ Efficient algorithms for computing the triplet and
quartet distance between trees of arbitrary degree ”; p. 1814-1832

Few Definitions before you dive into the code.

Newick Format & Parsing:
Newick tree format (or Newick notation or New Hampshire tree format) is a way of
representing g raph-theoretical trees with edge lengths using parentheses and
commas. Egs of files with Newick format would be
1. “ ((A,(B,C)),(D,E))” (all leaf nodes are named) ;
2. (A:0.1,B:0.2,(C:0.3,D:0.4)E:0.5)F (distances from the root and all names).
We parsed this tree using the Grammar Definition for Newick format to create an
Unrooted Tree.

Rooted and Unrooted Trees:
A rooted binary tree that is rooted on an internal node has exactly two immediate
descendant nodes for each internal node. An unrooted binary tree that is rooted on an
arbitrary internal node has exactly three immediate descendant nodes for the root node,
and each other internal node has exactly two immediate descendant nodes. A binary tree
rooted from a leaf has at most one immediate descendant node for the root node, and
each internal node has exactly two immediate descendant nodes.

Triplet and Quartet Distance:
Triplet Distance is between set of three leaves for two trees. Quartet Distance is between
set of four leaves. Distance here is calculated as number of triplets/quartets with
different structure in the two trees.

Our codebase is highly inspired from :

Andreas Sand, Morten K. Holt, Jens Johansen, Gerth Stølting Brodal, Thomas
Mailund and Christian N.S. Pedersen; “t qDist: A Library for Computing the
Quartet and Triplet Distances Between Binary or General Trees” ,
Bioinformatics, 2014, xx(yy), pp. ii-jj, doi: 10.1093/bioinformatics/btu157.

