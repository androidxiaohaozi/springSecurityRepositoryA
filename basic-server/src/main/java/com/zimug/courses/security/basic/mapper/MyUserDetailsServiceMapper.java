package com.zimug.courses.security.basic.mapper;

import com.zimug.courses.security.basic.model.MyUserDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MyUserDetailsServiceMapper {


    @Select("select username, password, enabled\n" +
            "from sys_user u\n" +
             "where u.username = #{userId}")
    MyUserDetails findByUserName(@Param("userId") String userId);

    @Select("select role_code\n" +
            "from sys_role r\n" +
            "left join sys_user_role ur on r.id = ur.role_id\n" +
            "left join sys_user u on u.id = ur.user_id\n" +
            "where u.username = #{userId}")
    List<String> findRoleByUserName(@Param("userId") String userId);


    @Select({
            "<script>",
            "select url ",
            "from sys_menu m",
            "left join sys_role_menu rm on m.id = rm.menu_id",
            "left join sys_role r on r.id = rm.role_id",
            "where r.role_code in ",
            "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>",
               "#{roleCode}",
            "</foreach>",
            "</script>"
    })
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);
}
