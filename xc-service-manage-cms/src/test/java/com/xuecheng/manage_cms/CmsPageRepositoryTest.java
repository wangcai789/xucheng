package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 查询全部
     */
    @Test
    public void testFindAll(){
        List<CmsPage> list = cmsPageRepository.findAll();
        System.out.println(list.size());
    }

    /**
     * 分页查询
     */
    @Test
    public void testFindPage() {
        int page = 0;//页数，从0开始
        int size = 10;//每页显示的大小
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> list = cmsPageRepository.findAll(pageable);
        System.out.println(list.getTotalPages());
    }

    /**
     * 保存
     */
    @Test
    public void testSave() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("save test");
        cmsPage.setPageAliase("hehe");
        cmsPageRepository.save(cmsPage);
    }

    /**
     * 根据id更新
     */
    @Test
    public void testUpdate() {
        //根据id获得
        Optional<CmsPage> optional = cmsPageRepository.findById("5c20d240c7213b10f870cfcb");
        if(optional.isPresent()) {//如果不为空
            CmsPage cmsPage = optional.get();
            System.out.println(cmsPage);
            //更新
            cmsPage.setPageName("update test");
            cmsPageRepository.save(cmsPage);
        }
    }
    /**
     * 根据条件获得
     */
    @Test
    public void testFind() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("update test");
        Example<CmsPage> example = Example.of(cmsPage);
        Optional<CmsPage> optional = cmsPageRepository.findOne(example);
        if(optional.isPresent()){
            System.out.println(optional.get());
        }
    }
    /**
     * 根据条件获得
     *   站点Id：精确匹配
     *   模板Id：精确匹配
     *   页面别名：模糊匹配
     */
    @Test
    public void testFind1() {

        Pageable pageable = PageRequest.of(0,10);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
        cmsPage.setPageAliase("轮播");

        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> list = cmsPageRepository.findAll(example, pageable);
        System.out.println(list.getTotalElements());
    }


    /**
     * 删除
     */
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5c20d240c7213b10f870cfcb");
    }
}
