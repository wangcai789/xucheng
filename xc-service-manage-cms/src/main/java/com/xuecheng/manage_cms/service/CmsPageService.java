package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    //分页查询
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        //创建模糊查询的对象
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

        //封装查询参数
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotBlank(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());//根据别名模糊查询
        }
        if(StringUtils.isNotBlank(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());//根据模板id精确查询
        }
        if(StringUtils.isNotBlank(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());//根据站点id精确查询
        }

        //创建查询条件对象
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

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
        Page<CmsPage> list = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(list.getTotalElements());
        queryResult.setList(list.getContent());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return responseResult;
    }

    //保存
    public CmsPageResult save(CmsPage cmsPage){
        if(cmsPage==null){
            return new CmsPageResult(CommonCode.INVALID_PARAM,null);
        }
        //在cms_page集中上创建页面名称、站点Id、页面webpath为唯一索引
        CmsPage resultCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(resultCmsPage!=null){
            //页面已存在
            return new CmsPageResult(CmsCode.CMS_ADDPAGE_EXISTSNAME,resultCmsPage);
        }
        //页面不存在--实现添加功能
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }

    //根据id查询页面
    public CmsPage findById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    //修改页面
    public CmsPageResult update(String id, CmsPage cmsPage){
        CmsPage cmsPage1 = findById(id);
        if(cmsPage1==null){
            return new CmsPageResult(CommonCode.INVALID_PARAM,null);
        }
        //更新模板id
        cmsPage1.setTemplateId(cmsPage.getTemplateId());
        //更新所属站点
        cmsPage1.setSiteId(cmsPage.getSiteId());
        //更新页面别名
        cmsPage1.setPageAliase(cmsPage.getPageAliase());
        //更新页面名称
        cmsPage1.setPageName(cmsPage.getPageName());
        //更新访问路径
        cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
        //更新物理路径
        cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        //执行更新
        CmsPage result = cmsPageRepository.save(cmsPage1);
        if(result!=null){
            return new CmsPageResult(CommonCode.SUCCESS,result);
        }
        return new CmsPageResult(CommonCode.FAIL,result);

    }

    //根据id删除页面
    public ResponseResult delete(String id){
        CmsPage cmsPage = findById(id);
        if(cmsPage!=null){
            cmsPageRepository.delete(cmsPage);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }
}
