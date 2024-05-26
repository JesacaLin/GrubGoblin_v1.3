# Functional Requirements for GrubGoblin API

### Access Level 1: Unauthenticated Users
- Register a new user account.

### Access Level 2: Authenticated Users with No Roles
- Retrieve a list of all available public deals.
- Retrieve a specific deal by its ID.
- Retrieve a list of deals with full details.
- Search for deals by keyword.
- View top-rated deals.
- Retrieve a list of all deals available at a specific place.
- Retrieve a list of all places offering deals.
- Find a specific place by its ID.
- View reviews for specific deals by deal ID.
- Retrieve a list of all reviews.
- View a specific review by its ID.

### Access Level 3: Authenticated Users with Contributor Role
- Includes all functionalities of Access Level 2.
- Update their own user account details.
- Create a new deal.
- Update their own deals.
- Create a new place.
- Update details of places they've created.
- Write a review for a deal.
- Update their own reviews.

### Access Level 4: Authenticated Users with Admin Role
- Includes all functionalities of Access Level 2 and 3.
- Retrieve a list of all users.
- Retrieve a user by their username.
- Delete a user.
- Retrieve a list of a user’s roles.
- Assign a role to a user.
- Remove a role from a user.
- Delete any deal.
- Delete any place.
- Delete any review.

### Additional Considerations
- An option may be added to make a deal “public”, making it visible to authenticated users with no roles.
- A “Private” flag could be implemented, making deals visible only to the user who created the entry.

