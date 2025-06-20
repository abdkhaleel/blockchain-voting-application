# Blockchain Voting System API

This repository contains the backend API for a secure, robust, and modern Blockchain-Powered Electronic Voting System. The system is designed using a hybrid architecture, leveraging a high-performance PostgreSQL database for real-time operations and a simulated blockchain for immutable anchoring and verification of election results.

This API provides a comprehensive set of endpoints for managing users, elections, candidates, voter eligibility, and the core voting process itself.

## Core Features Implemented

The system is being built in phases. The following key features are currently implemented and stable:

### 1. 🛡️ Authentication & Authorization
- **JWT-Based Security:** State-of-the-art security using JSON Web Tokens (JWT) for stateless authentication.
- **Role-Based Access Control (RBAC):** Clear distinction between `USER` and `ADMIN` roles, with endpoints protected using method-level security (`@PreAuthorize`).
- **User Registration:** Secure user registration with password hashing using BCrypt.
- **Admin & User Roles:** Flexible registration system capable of assigning `USER` or `ADMIN` roles.

### 2. 🗳️ Election Management
- **Full CRUD Operations:** Administrators can create, read, update, and delete elections.
- **Detailed Election Information:** Elections store critical data such as title, description, start/end dates, and status (`PENDING`, `ACTIVE`, `COMPLETED`).
- **Public Listing:** Endpoints are available to publicly list all elections with support for pagination and sorting.

### 3. 👥 Candidate Management
- **Election-Specific Candidates:** Candidates are strongly associated with a specific election.
- **Full CRUD Operations:** Administrators can add, view, update, and remove candidates for any given election.
- **Public Information:** Candidate lists for any election are publicly viewable.

### 4. 📝 Voter Eligibility Management
- **Private Election Support:** Admins can manage a list of eligible voters for any specific election.
- **Eligibility Control:** Endpoints allow admins to add or remove a registered user's eligibility to vote in an election.
- **Self-Check:** Authenticated users can check their own eligibility for a specific election.

### 5. ✓ Core Voting Operations
- **Secure Vote Casting:** Eligible users can cast a single, unique vote for a candidate in an active election. The system prevents duplicate voting at the database level.
- **Vote Status Check:** Users can verify whether they have already cast their vote in a particular election.
- **Vote Retraction:** Users have the ability to retract their vote before the election closes.

### 6. 📊 Transparent Election Results
- **Public Results Endpoint:** A dedicated public endpoint to view the results of any election.
- **Live Vote Tally:** Returns a list of all candidates in the election along with their current vote count.
- **Complete Transparency:** Ensures all candidates are included in the results, even those with zero votes, providing a full and honest tally.

### 7. ⚓ Secure Election Anchoring
- **Admin-Only Operation:** A secure endpoint allows administrators to formally complete an election.
- **Cryptographic Proof:** Generates a unique SHA-256 hash of the final, aggregated election results.
- **Blockchain Anchoring:** Stores this hash in a new block on a simulated blockchain, creating a permanent, tamper-evident seal.
- **Result Immutability:** Guarantees that the declared final results can be verified and proven to have not been altered after the election's closure.


## Technology Stack

- **Backend:** [Spring Boot 3.3.0](https://spring.io/projects/spring-boot)
- **Language:** [Java 17](https://www.oracle.com/java/technologies/javase/17-archive-downloads.html)
- **Database:** [PostgreSQL](https://www.postgresql.org/)
- **Security:** [Spring Security 6](https://spring.io/projects/spring-security), JWT
- **Data Access:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa), Hibernate
- **Build Tool:** [Apache Maven](https://maven.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/) & Docker Compose

## Verifying Election Integrity

The blockchain provides a mechanism for any administrator or auditor to verify that the election results stored in the database have not been tampered with after an election was completed.

The process is as follows:

1.  **Get the Anchor Hash:** Use the `GET /api/blockchain` endpoint (admin-only) to retrieve the entire chain. Find the block corresponding to the election and copy the `data` field. This is the official, immutable hash of the results (`HASH_FROM_CHAIN`).
2.  **Get Current Results:** Use the public `GET /api/elections/{id}/results` endpoint to fetch the current results as reported by the database.
3.  **Re-create the Hash String:** Based on the results from step 2, construct a deterministic string by sorting candidates by their ID. For example: `electionId:101;candidateId1:votes1;candidateId2:votes2;`.
4.  **Recalculate the Hash:** Use an external tool (like an online SHA-256 generator) to calculate the hash of the string from step 3 (`RECALCULATED_HASH`).
5.  **Compare:** If `HASH_FROM_CHAIN` is identical to `RECALCULATED_HASH`, the results are valid. If they differ, the database has been tampered with.

## API Endpoints Summary

### Authentication (`/api/auth`, `/api/users`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/users/register` | Register a new user or admin. | Public |
| `POST` | `/api/auth/login` | Authenticate a user and receive a JWT. | Public |

### Election Management (`/api/elections`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/elections` | Create a new election. | Admin |
| `GET` | `/api/elections` | Get a paginated list of all elections. | Public |
| `GET` | `/api/elections/{id}` | Get details of a single election. | Public |
| `GET` | `/api/elections/{id}/results` | Get the vote tally for an election. | Public |
| `POST` | `/api/elections/{id}/complete` | Completes an election and anchors its results to the blockchain. | Admin |
| `PUT` | `/api/elections/{id}` | Update an existing election. | Admin |
| `DELETE`| `/api/elections/{id}` | Delete an election. | Admin |

### Blockchain (`/api/blockchain`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/blockchain` | View the entire blockchain ledger. | Admin |

### Candidate Management (`/api/elections`, `/api/candidates`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/elections/{eid}/candidates` | Add a candidate to an election. | Admin |
| `GET` | `/api/elections/{eid}/candidates` | Get all candidates for an election. | Public |
| `GET` | `/api/candidates/{cid}` | Get a single candidate by their ID. | Public |
| `PUT` | `/api/elections/{eid}/candidates/{cid}` | Update a candidate's details. | Admin |
| `DELETE`| `/api/elections/{eid}/candidates/{cid}`| Remove a candidate from an election. | Admin |

### Voter Eligibility (`/api/elections/{eid}/voters`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/elections/{eid}/voters/{uid}` | Make a user eligible to vote. | Admin |
| `DELETE` | `/api/elections/{eid}/voters/{uid}` | Remove a user's eligibility. | Admin |
| `GET` | `/api/elections/{eid}/voters` | Get a list of eligible voters. | Admin |
| `GET` | `/api/elections/{eid}/eligibility` | Check if the current user is eligible. | User |

### Voting Operations (`/api/votes`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/votes` | Cast a vote in an election. | User |
| `GET` | `/api/votes/check/{eid}` | Check if the current user has voted. | User |
| `DELETE` | `/api/votes/{eid}` | Retract the current user's vote. | User |

## Getting Started

### Prerequisites
- [JDK 17](https://adoptium.net/temurin/releases/) or newer
- [Docker](https://www.docker.com/products/docker-desktop/) and Docker Compose
- [Apache Maven](https://maven.apache.org/download.cgi)
- An IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- A REST client like [Postman](https://www.postman.com/) or [Insomnia](https://insomnia.rest/)

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/abdkhaleel/blockchain-voting-application.git
    cd blockchain-voting-application
    ```

2.  **Start the Database:**
    The project includes a `docker-compose.yml` file to easily run a PostgreSQL database instance.
    ```bash
    docker-compose up -d
    ```
    This will start a PostgreSQL server on `localhost:5432`.

3.  **Configure the Application:**
    The database connection properties are already configured in `src/main/resources/application.properties`. You can modify them if your setup is different.

4.  **Build and Run the Application:**
    You can run the application using your IDE by running the `main` method in `BlockchainVotingApplication.java`, or by using the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
    The API will be available at `http://localhost:8080`.

## Future Work

The project is actively being developed. Upcoming features include:
- Enhanced user profile management.
- Bulk operations for adding voters and candidates.
- System-wide notifications and audit logging.
- Advanced analytics and real-time results dashboards.