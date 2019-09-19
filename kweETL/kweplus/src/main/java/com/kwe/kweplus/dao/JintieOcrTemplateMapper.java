package com.kwe.kweplus.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kwe.kweplus.modal.JintieOcrTemplate;
import com.kwe.kweplus.modal.JintieOcrTemplateParm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JintieOcrTemplateMapper {
    /**
     *  通过主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  新增
     * @param record
     * @return
     */
    int insert(JintieOcrTemplate record);

    /**
     *  通过主键查询
     * @param id
     * @return
     */
    JintieOcrTemplate selectByPrimaryKey(Integer id);

    /**
     *  通过ID查询
     * @param templateId
     * @return
     */
    List<JintieOcrTemplate> selectByTemplateId(String templateId);

    /**
     *  查询全部
     * @return
     */
    List<JintieOcrTemplate> selectAll();

    /**
     *  通过主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(JintieOcrTemplate record);

    /**
     *  通过参数查询
     * @param p
     * @param parm
     * @return
     */
    List<JintieOcrTemplate> selectByParm(Page<JintieOcrTemplate> p,@Param("parm") JintieOcrTemplateParm parm);
}