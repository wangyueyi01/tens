package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询列表
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 增加
     * @param label
     */
    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 修改
     * @param label
     */
    public void update(Label label){

        labelDao.save(label);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id){

        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            /**
             * @param root 根对象 也就是说要把条件封装到那个对象中
             * @param query 封装的都是查询关键字，比如group by ，order by等
             * @param cb 用来封装条件对象的，如果直接返回null，表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //list集合存放查询条件
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname()!=null&&!"".equals(label.getLabelname())){
                Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                list.add(predicate);
                }
                if(label.getState()!=null&&!"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);

                }
                //new一个数组座位最终返回的条件
                Predicate [] parr = new Predicate[list.size()];
                list.toArray(parr);
                return cb.and(parr);
            }
        });
    }

    public Page<Label> findQuery(Label label, int page, int size) {
        //封装分页对象 pageable是springboot的分页工具
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(new Specification<Label>() {
            /**
             * @param root 根对象 也就是说要把条件封装到那个对象中
             * @param query 封装的都是查询关键字，比如group by ，order by等
             * @param cb 用来封装条件对象的，如果直接返回null，表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //list集合存放查询条件
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname()!=null&&!"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if(label.getState()!=null&&!"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);

                }
                //new一个数组座位最终返回的条件
                Predicate [] parr = new Predicate[list.size()];
                list.toArray(parr);
                return cb.and(parr);
            }
        },pageable);

    }
}
