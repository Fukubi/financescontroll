package com.learning.financescontroll.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.learning.financescontroll.entity.UserEntity;

public class ResourceOwner implements UserDetails {

	private static final long serialVersionUID = -1769229667184789310L;

	private UserEntity usuario;

	public ResourceOwner(UserEntity usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(this.usuario.getRole()));
		return roles;
	}

	@Override
	public String getPassword() {
		return this.usuario.getCredenciais().getPassword();
	}

	@Override
	public String getUsername() {
		return this.usuario.getCredenciais().getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
