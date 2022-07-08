package com.gssk.gssk.Google_related;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface user_info_repo extends CrudRepository<user_info,String> {


}
