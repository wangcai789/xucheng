package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        //前台页面传递过来的页数是从1开始的
        //分页查询页数从0开始
        page = page-1;
        if(page<0){
            page = 0;
        }
        if(size<=0){
            size=20;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> list = cmsPageRepository.findAll(pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(list.getTotalElements());
        queryResult.setList(list.getContent());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }
}
