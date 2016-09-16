Movie recommending system (algorithms-assign2)

The program can be run from Driver class in controller package

Requirements from the specs were accomplished, some noticeable features are: 
-HashMap(ID -> Entity) and HashSet are mostly used for speed and to avoid overhead of duplicate entities (especially when adding), ID is not included in hashCode() otherwise those map and set would have issues. However, there are lists that store the ids such that traversing those will get the entities in a desired sequence.
-Both XML and JSON serializer are available
-All IDs are controlled by model classes (User and Movie) to avoid confliction from external data import.
-To further minimize confliction, CSV File loader has ID translators to record the mapping between IDs in the file and actual IDs in the program (CSV file loader can only be used once !)
-Any error (eg. ID duplication, empty entity, etc.) existing in the CSV file will cause an Exception (all of them were removed, most of those are recorded in foundErrors.txt within data_movieLens folder) 
-Everytime a user or movie is added, binary search is used to find the appropriate position in the list to add in (basically when left pointer meets right pointer is the position to add) - Binary insertion sort ?
=> overall time taken to load a set of entities from the CSV file would take nlog(n) 

Recommendation algorithm, eg. get recommendation for user A:
1) Find the potentially similar users set
->retrieving all of user A's positive ratings (ratings that have rating point above 0), 
->from each rating trace back to the movie, record the genres, and retrieve all users that have rated that movie that same rating point
2) Calculate similarity between user A and every entity in the set of step 1, an example of user A vs user B:
->again using all the ratings of user A to create a vector, called vector U
->from each rating, find the movie and check whether user B has rated the movie. If yes, get the rating point, otherwise the rating point will be considered 0. This creates another vector, called vector V
(doing this ensures that every element in vector U corresponds to the right element in vector V, and the size of these two are equal)
->similarity is calculated in Matrix class, utils package
3) Now that U and V are in the same dimension, work out the angle between U and V
4) Doing step 3 results in a set of angles, pick out the minimum angle (should be less than 90 degree, which is orthogonal) indicating a pair of vectors that more or less point to the same direction
5) Doing step 4 will get to the most similar user to user A. Retrieve all positive ratings and get all movies from those ratings from this user and use the favorite genres of user A (collected in step 1) to filter movies to achieve a smaller set
6) Return the set, which contains the recommended movies

Extra features:
-Quick recommendation: using the same algorithm as above, but the latest (using the timestamp) and highest rating that the user makes will define the genre filter to further limit the recommeneded set
-Query interface is used to construct an incomplete User or Movie object and treat it as a Query object when doing searching (user cannot give all of the details - only a temporary object is needed)
-Ripple search: given an index in a list, this would spread out left and right to find similar entities 
