package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.Status;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.SignUpRequestDto;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T22:30:14+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserInfoDto toUserInfoDto(User user) {
        if ( user == null ) {
            return null;
        }

        String email = null;
        String name = null;
        String phone = null;
        String address = null;
        Role role = null;
        Status status = null;
        Integer loginFailCount = null;

        email = user.getEmail();
        name = user.getName();
        phone = user.getPhone();
        address = user.getAddress();
        role = user.getRole();
        status = user.getStatus();
        loginFailCount = user.getLoginFailCount();

        UserInfoDto userInfoDto = new UserInfoDto( email, name, phone, address, role, status, loginFailCount );

        return userInfoDto;
    }

    @Override
    public FindUsernameDto toFindUsernameDto(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;

        username = user.getUsername();

        FindUsernameDto findUsernameDto = new FindUsernameDto( username );

        return findUsernameDto;
    }

    @Override
    public User toUser(SignUpRequestDto source, String encodedPassword) {
        if ( source == null && encodedPassword == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        if ( source != null ) {
            user.username( source.username() );
            user.email( source.email() );
            user.name( source.name() );
            user.phone( source.phone() );
            user.address( source.address() );
        }
        user.password( encodedPassword );

        return user.build();
    }
}
