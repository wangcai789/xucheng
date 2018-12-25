package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    //分页查询
    @Override
    @PostMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody QueryPageRequest pageRequest) {
       return cmsPageService.findList(page,size,pageRequest);
    }

    //保存
    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    //根据id查询
    @Override
    @GetMapping("/findById/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.findById(id);
    }

    //更新
    @Override
    @PutMapping("/update/{id}")
    public CmsPageResult update(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }
}
