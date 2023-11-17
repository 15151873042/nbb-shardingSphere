package com.nbb.databaseHorizontal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nbb.databaseHorizontal.entity.User;
import com.nbb.databaseHorizontal.mapper.UserMapper;
import com.nbb.databaseHorizontal.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2023-11-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
