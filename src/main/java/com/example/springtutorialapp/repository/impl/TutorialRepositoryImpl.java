package com.example.springtutorialapp.repository.impl;

import com.example.springtutorialapp.model.Tutorial;
import com.example.springtutorialapp.repository.inter.TutorialRepositoryInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TutorialRepositoryImpl implements TutorialRepositoryInter{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TutorialRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Tutorial book) {
        return jdbcTemplate.update("insert into tutorial(title,description,published) values(?,?,?)",
                new Object[]{book.getTitle(),book.getDescription(),book.isPublished()});
    }

    @Override
    public int update(Tutorial book) {
        return jdbcTemplate.update("update tutorial set title=?,description=?,published=? where id=?",
                new Object[]{book.getTitle(),book.getDescription(),book.isPublished(),book.getId()});
    }

    @Override
    public Tutorial findById(Long id) {
        try{
        Tutorial tutorial=jdbcTemplate.queryForObject("select * from tutorial where id=?",
                BeanPropertyRowMapper.newInstance(Tutorial.class),id);
        return tutorial;
        }catch (IncorrectResultSizeDataAccessException e){
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from tutorial where id=?",id);
    }

    @Override
    public List<Tutorial> findAll() {
        return jdbcTemplate.query("select * from tutorial",BeanPropertyRowMapper.newInstance(Tutorial.class));
    }

    @Override
    public List<Tutorial> findByPublished(boolean published) {
        return jdbcTemplate.query("select * from tutorial where published=?",BeanPropertyRowMapper.newInstance(Tutorial.class),published);
    }

    @Override
    public List<Tutorial> findByTitleContaining(String title) {
        return jdbcTemplate.query("select * from tutorial where title=?",BeanPropertyRowMapper.newInstance(Tutorial.class),title);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("delete from tutorial");
    }
}
