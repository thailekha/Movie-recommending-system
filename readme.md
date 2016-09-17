#Movie recommending system (algorithms-assign2)

**Assignment 2 for Algorithms module**

The program can be run from Driver class in controller package

This project was developed by following Test-Driven Development technique including multiple iterations, each ieration implements a use case, there are 10 basic use cases implemented:
- add a user
- add a movie
- remove a user
- get a movies details
- rate a movie
- get a users ratings
- get the top ten movies (by all ratings)
- for a given user, get recommendations for that user (recommendation algorithm)
- read the movie db from an external css file
- save / load the main application model

**An illustration of the Recommendation algorithm:**

*1) Find the potentially similar users set*
- Retrieve all of user A's positive ratings (ratings that have rating point above 0)
- From each rating, retrieve the rated movie, record the genres, and retrieve all users that have rated that movie that same rating point

*2) Calculate similarity between user A and every entity in the set resulted from step 1, an example of finding similarity between user A and user B:*
- Again using all the ratings of user A to create a vector, called vector U
- From each rating, retrieve the rated movie and check whether user B has rated it. If yes, get the rating point, otherwise the rating point will be considered 0. This process ultimately creates another vector, called vector V
(need to ensure that every rating of a movie in vector U corresponds to the rating of the same movie in vector V, and the size of these vector are equal)
- Similarity is then calculated in Matrix class, utils package
*3) Now that U and V are in the same dimension, work out the angle between U and V*

*4) Doing step 3 results in a set of angles of user A vs other users in the sytem, pick out the minimum angle (should be less than 90 degree, which is orthogonal), resulting in vectors that more or less point to the same direction*

*5) Doing step 4 will get to the most similar user to user A. Retrieve all positive ratings and get all movies from those ratings from this user and use the favorite genres of user A (collected in step 1) to filter movies to achieve a smaller set*

*6) Return the set, which contains the recommended movies*

**Requirements from the assignment specs were satisfied, some noticeable features are:**
- HashMap(ID -> Entity) and HashSet are mostly used for speed and to avoid overhead of duplicate entities (especially when adding) However, there are lists storing the ids in a way that iterating over those can retrieve the entities in a special sequence, as if the entities were stored in a sorted list
- Both XML and JSON serializer are available
- All IDs are controlled by model classes (User and Movie) to avoid confliction from external data import.
- To further minimize confliction, CSV File loader has ID translators to record the mapping between IDs in the file and actual IDs in the program (CSV file loader can only be used once !)
- Any error (eg. ID duplication, empty entity, etc.) existing in a CSV file will cause an Exception. The CSV files in the repository have all such errors removed, most of those are recorded in foundErrors.txt within data_movieLens folder) 
- Everytime a user or movie is added, binary search is used to find the appropriate position in the IDs list to add in (basically the point where the "left pointer" meets "right pointer" in binary search is the position to add), so the overall time taken to load a set of n entities from the CSV file would take nlog(n) 
- Quick recommendation: using the same algorithm as above, but from the latest (using the timestamp) and highest rating that the user has made, the genre of the rated movie is retrieved, which is used to further filter the recommeneded movie set
- Query interface is used to construct an incomplete User or Movie object and treat it as a Query object when doing searching (user cannot give all of the details - only a temporary object is needed)
- Ripple search: given an index in a list, this would spread out left and right to find similar entities 
