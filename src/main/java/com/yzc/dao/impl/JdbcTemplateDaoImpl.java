package com.yzc.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yzc.dao.JdbcTemplateDao;
import com.yzc.entity.Student;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;

@Repository
public class JdbcTemplateDaoImpl implements JdbcTemplateDao {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateDaoImpl.class);

    @Qualifier(value = "jdbcTemplate")
    @Autowired
    private JdbcTemplate defaultJdbcTemplate;

    @Override
    public boolean createStudent(final Student student) {

        //如果使用命名参数，则sql="insert into ace_student(id) values(:id)"
        String sql = "insert into ace_student(identifier,state,create_time, update_time,title,description,username,password,student_name,sex,school,grade,classes) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        LOG.info("查询的SQL语句：" + sql.toString());
        defaultJdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, student.getIdentifier());
                ps.setInt(2, student.getState().getValue());
                ps.setObject(3, student.getCreateTime());
                ps.setObject(4, student.getUpdateTime());
                ps.setString(5, student.getTitle());
                ps.setString(6, student.getDescription());
                ps.setString(7, student.getUsername());
                ps.setString(8, student.getPassword());
                ps.setString(9, student.getName());
                ps.setInt(10, student.getSex());
                ps.setString(11, student.getSchool());
                ps.setString(12, student.getGrade());
                ps.setString(13, student.getClasses());
            }
        });

        return true;
    }

    @Override
    public boolean updateStudent(final Student student) {

        String sql = "update ace_student set update_time=?,title=?,description=?,username=?,password=?,student_name=?,sex=?,school=?,grade=?,classes=? where identifier=?";
        LOG.info("查询的SQL语句：" + sql);
        defaultJdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {

                ps.setObject(1, student.getUpdateTime());
                ps.setString(2, student.getTitle());
                ps.setString(3, student.getDescription());
                ps.setString(4, student.getUsername());
                ps.setString(5, student.getPassword());
                ps.setString(6, student.getName());
                ps.setInt(7, student.getSex());
                ps.setString(8, student.getSchool());
                ps.setString(9, student.getGrade());
                ps.setString(10, student.getClasses());
                ps.setString(11, student.getIdentifier());
            }
        });
        return true;
    }

    @Override
    public boolean deleteStudent(Student student) {

        String sql = "delete from ace_student where identifier=? or username=?";
        defaultJdbcTemplate.update(sql, new Object[]{student.getIdentifier(), student.getUsername()}, new int[]{java.sql.Types.VARCHAR, java.sql.Types.VARCHAR});
        return true;
    }

    @Override
    public boolean deleteStudent(String id) {

        String sql = "delete from ace_student where identifier=?";
        defaultJdbcTemplate.update(sql, new Object[]{id}, new int[]{java.sql.Types.VARCHAR});
        return true;
    }

    @Override
    public Student queryStudentById(String id) {

        // queryForObject方法在查询结果为空的时候不是返回null，而是抛出一个EmptyResultDataAccessException
        // 为了避免出现以上的异常，最好还是使用queryForList API，即使返回的结果集合的size为0，即Zero
        // Row，也不会抛出异常。如果size不为0，即可以使用get(0)取得第一个查询对象
        final List<Student> resultList = new ArrayList<Student>();
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select * from ace_student where identifier=:id";
        params.put("id", id);
        LOG.info("查询的SQL语句：" + sql.toString());
        LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
        namedJdbcTemplate.query(sql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setIdentifier(rs.getString("identifier"));
                student.setCreateTime(rs.getDate("create_time"));
                student.setUpdateTime(rs.getDate("update_time"));
                student.setTitle(rs.getString("title"));
                student.setDescription(rs.getString("description"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("student_name"));
                student.setSex(rs.getInt("sex"));
                student.setSchool(rs.getString("school"));
                student.setGrade(rs.getString("grade"));
                student.setClasses(rs.getString("classes"));
                resultList.add(student);
                return null;
            }
        });
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);

    }

    @Override
    public Student queryStudentByUsername(String username) {

        final List<Student> resultList = new ArrayList<Student>();
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select * from ace_student where username=:username";
        params.put("username", username);
        LOG.info("查询的SQL语句：" + sql.toString());
        LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
        namedJdbcTemplate.query(sql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setIdentifier(rs.getString("identifier"));
                student.setCreateTime(rs.getDate("create_time"));
                student.setUpdateTime(rs.getDate("update_time"));
                student.setTitle(rs.getString("title"));
                student.setDescription(rs.getString("description"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("student_name"));
                student.setSex(rs.getInt("sex"));
                student.setSchool(rs.getString("school"));
                student.setGrade(rs.getString("grade"));
                student.setClasses(rs.getString("classes"));
                resultList.add(student);
                return null;
            }
        });
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public List<Student> queryStudentItems(String words, String limit) {

        final List<Student> resultList = new ArrayList<Student>();
        Map<String, Object> params = new HashMap<String, Object>();
        Integer result[] = ParamCheckUtil.checkLimit(limit);
        String sqlLimit = " LIMIT " + result[0] + "," + result[1];
        String querySql = "select * from ace_student";

        if (StringUtils.hasText(words)) {
            querySql += " WHERE student_name like :words or username like :words or title like :words or description like :words";
            params.put("words", words);
            LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        }
        querySql += sqlLimit;
        LOG.info("查询的SQL语句：" + querySql.toString());
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
        namedJdbcTemplate.query(querySql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setIdentifier(rs.getString("identifier"));
                student.setCreateTime(rs.getDate("create_time"));
                student.setUpdateTime(rs.getDate("update_time"));
                student.setTitle(rs.getString("title"));
                student.setDescription(rs.getString("description"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("student_name"));
                student.setSex(rs.getInt("sex"));
                student.setSchool(rs.getString("school"));
                student.setGrade(rs.getString("grade"));
                student.setClasses(rs.getString("classes"));
                resultList.add(student);
                return null;
            }
        });
        return resultList;
    }

    @Override
    public Long queryStudentTotal(String words, String limit) {

        String querySql = "select count(*) from ace_student";
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.hasText(words)) {
            querySql += " WHERE student_name like :words or username like :words or title like :words or description like :words";
            params.put("words", words);
            LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        }
        LOG.info("查询的SQL语句：" + querySql.toString());
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
        @SuppressWarnings("deprecation")
        long total = namedJdbcTemplate.queryForLong(querySql, params);
        return total;
    }

}
