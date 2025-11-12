package com.example.mapper;

import com.example.entity.EmployeeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    // --- CRUD (Annotation Based) ---
    @Insert("INSERT INTO employee (name, salary, department_id, project_id) VALUES (#{name}, #{salary}, #{departmentId}, #{projectId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(EmployeeEntity employee);

    @Update("UPDATE employee SET name=#{name}, salary=#{salary}, department_id=#{departmentId}, project_id=#{projectId} WHERE id=#{id}")
    void update(EmployeeEntity employee);

    @Delete("DELETE FROM employee WHERE id=#{id}")
    void delete(Integer id);

    @Select("SELECT * FROM employee WHERE id=#{id}")
    EmployeeEntity findById(Integer id);

    // Fetch single employee with department & project names
    @Select("""
    SELECT e.id, e.name, e.salary,
           e.department_id, e.project_id,
           d.name AS department_name,
           p.name AS project_name
    FROM employee e
    JOIN department d ON e.department_id = d.id
    JOIN project p ON e.project_id = p.id
    WHERE e.id = #{id}
""")
    @Results({
            @Result(column="id", property="id"),
            @Result(column="name", property="name"),
            @Result(column="salary", property="salary"),
            @Result(column="department_id", property="departmentId"),
            @Result(column="project_id", property="projectId"),
            @Result(column="department_name", property="departmentName"),
            @Result(column="project_name", property="projectName")
    })
    EmployeeEntity findByIdWithJoin(Integer id);

    // --- Pagination, Sorting, Search (Dynamic via XML) ---
    List<EmployeeEntity> searchEmployees(@Param("search") String search,
                                         @Param("sortBy") String sortBy,
                                         @Param("sortOrder") String sortOrder,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    // --- Complex Join Example (Defined in XML) ---
    List<EmployeeEntity> findAllWithDepartmentAndProject();
}
