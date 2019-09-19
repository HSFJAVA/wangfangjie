package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kwe.kweplus.modal.JintieModeParm;
import com.kwe.kweplus.modal.JintieModelList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JintieModelListMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jintie_model_list
     *
     * @mbg.generated Wed Jun 26 14:31:33 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jintie_model_list
     *
     * @mbg.generated Wed Jun 26 14:31:33 CST 2019
     */
    int insert(JintieModelList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jintie_model_list
     *
     * @mbg.generated Wed Jun 26 14:31:33 CST 2019
     */
    JintieModelList selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jintie_model_list
     *
     * @mbg.generated Wed Jun 26 14:31:33 CST 2019
     */
    List<JintieModelList> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jintie_model_list
     *
     * @mbg.generated Wed Jun 26 14:31:33 CST 2019
     */
    int updateByPrimaryKey(JintieModelList record);

    List<JintieModelList> selectByParm(Page<JintieModelList> page, @Param("parm")JintieModeParm parm);
    JintieModelList selectByModel_id(@Param("model_id") String model_id);
    int updateByModel_id(JintieModelList record);


    void updateStatus(@Param("model_id") String model_id);
}