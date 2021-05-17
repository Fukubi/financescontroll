package com.learning.financescontroll.v1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learning.financescontroll.core.impl.ResourceOwner;
import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.IUserRepository;

@Service
public class UserInfoService implements UserDetailsService {

	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserEntity> usuario = this.userRepository.findByCredenciaisUsername(username);

		if (usuario.isPresent()) {
			UserDetails userDetails = new ResourceOwner(usuario.get());
			return userDetails;
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
	}

}
