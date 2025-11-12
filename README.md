# Spring Boot + MyBatis + PostgreSQL Example

This project demonstrates how Spring Boot integrates with MyBatis ORM using PostgreSQL as the database.
It includes CRUD operations, search with sorting & pagination, and advanced SQL queries using XML mapping.

<br>

## Tech Stack

| Layer | Technology |
|--------|-------------|
| Backend Framework | Spring Boot 3.5.7 |
| ORM / SQL Mapper | MyBatis 3.0.5 |
| Database | PostgreSQL |
| Validation | Jakarta Validation |
| Lombok | For reducing boilerplate code |
| Build Tool | Maven |
| Testing | Spring Boot Starter Test |

<br>

## Architecture Overview

Flow:
`Controller → DTO → Service → Mapper → Entity → Database (PostgreSQL)`

#### Controller
Handles HTTP requests and delegates to the service layer.

#### DTO
Used to transfer data between layers (`EmployeeRequestDTO`, `EmployeeResponseDTO`).

#### Service
Implements business logic and handles transactions.
Delegates persistence work to MyBatis mappers.

#### Mapper
Acts as repository (similar to Spring Data JPA repositories).
- Simple CRUD queries are defined using MyBatis annotations.
- Complex / dynamic queries are defined in external `.xml` mapper files.

#### Entity
Represents database tables (`EmployeeEntity`, `DepartmentEntity`, `ProjectEntity`).

<br>

## Why Use XML for Complex Queries?

While annotations are convenient for basic CRUD, complex queries often involve:
- Dynamic filtering (e.g., search)
- Conditional joins
- Sorting and pagination logic

These can clutter annotation-based SQL strings. MyBatis XML Mappers make complex SQL easier to read, maintain, and modify dynamically.

<br>

## Example of Basic CRUD (Annotation Based)

```java
@Select("SELECT * FROM employee WHERE id=#{id}")
EmployeeEntity findById(Integer id);

@Insert("INSERT INTO employee (name, salary, department_id, project_id) VALUES (#{name}, #{salary}, #{departmentId}, #{projectId})")
@Options(useGeneratedKeys = true, keyProperty = "id")
void insert(EmployeeEntity employee);
```

These queries are short and simple, hence defined directly via annotations.

<br>

## Example of Complex Queries (XML Based)

#### EmployeeMapper.xml

```xml
<select id="searchEmployees" resultType="com.example.entity.EmployeeEntity">
    SELECT e.*, d.name AS department_name, p.name AS project_name
    FROM employee e
    LEFT JOIN department d ON e.department_id = d.id
    LEFT JOIN project p ON e.project_id = p.id
    <where>
        <if test="search != null and search != ''">
            (LOWER(e.name) LIKE CONCAT('%', LOWER(#{search}), '%')
            OR LOWER(d.name) LIKE CONCAT('%', LOWER(#{search}), '%')
            OR LOWER(p.name) LIKE CONCAT('%', LOWER(#{search}), '%'))
        </if>
    </where>
    ORDER BY
    <choose>
        <when test="sortBy != null and sortBy != ''">${sortBy}</when>
        <otherwise>e.id</otherwise>
    </choose>
    <choose>
        <when test="sortOrder == 'desc'">DESC</when>
        <otherwise>ASC</otherwise>
    </choose>
    LIMIT #{limit} OFFSET #{offset}
</select>
```

#### Explanation
- `search`: Filters employees by name, department, or project.
- `ORDER BY`: Dynamic sorting using `sortBy` and `sortOrder`.
- `LIMIT` and `OFFSET`: Enable pagination.

#### Offset
`offset = (page - 1) * size`
It determines how many rows to skip before fetching the current page.
Example:
- Page = 1, Size = 10 -> Offset = 0
- Page = 2, Size = 10 -> Offset = 10

<br>

## Search API Flow

#### Endpoint:
```
GET /api/v1/employees/search
```

#### Parameters:
| Param | Description | Default |
|--------|-------------|----------|
| search | Filter by name, department, or project | (optional) |
| sortBy | Sort column | id |
| sortOrder | asc or desc | asc |
| page | Page number | 1 |
| size | Records per page | 10 |

#### Flow:
1. Controller receives search params.
2. Service computes `offset = (page - 1) * size`.
3. Mapper calls the XML query `searchEmployees`.
4. MyBatis dynamically builds SQL based on parameters.

<br>

## findById vs findByIdWithJoin methods

#### findById
```java
@Select("SELECT * FROM employee WHERE id=#{id}")
EmployeeEntity findById(Integer id);
```
Returns only the employee's basic info (no joined data).

#### findByIdWithJoin
```java
@Select("SELECT e.id, e.name, e.salary, e.department_id, e.project_id, d.name AS department_name, p.name AS project_name FROM employee e JOIN department d ON e.department_id = d.id JOIN project p ON e.project_id = p.id WHERE e.id = #{id}")
EmployeeEntity findByIdWithJoin(Integer id);
```
Returns employee info plus department name and project name using SQL JOIN.

#### Example Output:
```json
{
  "id": 1,
  "name": "Amit Sharma",
  "salary": 75000,
  "departmentName": "Engineering",
  "projectName": "Alpha Project"
}
```

<br>

## Transaction Management

Spring's `@Transactional` annotation (used in `EmployeeServiceImpl`) ensures:
- Atomicity: All DB operations within a transaction succeed or rollback together.

```java
@Override
@Transactional
public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
    EmployeeEntity entity = toEntity(dto);
    employeeMapper.insert(entity);
    return toResponseDTO(entity);
}
```

<br>

## How to Run the Project

#### Step 1: Clone the Repository
```bash
git clone https://github.com/SirajChaudhary/springboot-mybatis-postgre-example.git
cd springboot-mybatis-postgre-example
```

#### Step 2: Setup Database
First, create a new database in PostgreSQL (e.g. using pgAdmin client):
```sql
CREATE DATABASE exampledb;
```
Then execute the following SQL script to create tables and insert sample data:
```
src/main/resources/sample_data_postgre_db_exampledb.sql
```
This script will create the necessary tables in the database and populate them with sample data.

#### Step 3: Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

#### Step 4: Test APIs via Postman
Import the provided collection file:
`postman_collection.json`
and test the endpoints.

<br>

## API Endpoints Summary

| Method | Endpoint | Description |
|--------|-----------|-------------|
| POST | /api/v1/employees | Create employee |
| GET | /api/v1/employees/{id} | Get employee by ID |
| PUT | /api/v1/employees/{id} | Update employee |
| DELETE | /api/v1/employees/{id} | Delete employee |
| GET | /api/v1/employees/all | Get all employees with department/project |
| GET | /api/v1/employees/search | Search employees with pagination & sorting |

<br>

## Postman Collection
Following Postman collection file is available in the project root directory. You can import into Postman and test APIs:
```
postman_collection.json
```

<br>

## Conclusion

This project demonstrates:
- How Spring Boot integrates with MyBatis
- When to use annotation-based vs XML-based queries
- How to handle searching, sorting, and pagination
- Clean separation of layers using DTOs and entities
- Robust transaction handling using Spring

<br>

## License

Free Software, by [Siraj Chaudhary](https://www.linkedin.com/in/sirajchaudhary/)