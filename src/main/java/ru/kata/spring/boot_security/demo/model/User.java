package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails, GrantedAuthority {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "first_name")
   private String firstName;

   @Column(name = "last_name")
   private String lastName;

   @Column(name = "email")
   private String email;

   @NotEmpty(message = "Имя пользователя не должно быть пустым")
   @Size(min = 2, max = 100, message = "Имя пользователя должно быть от 2 до 100 символов")
   @Column(name = "username")
   private String username;

   @Column(name = "password")
   private String password;

   public User() {}
   
   public User(String firstName, String lastName, String email, String username) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.username = username;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getUsername() {
      return username;
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

   public void setUsername(String username) {
      this.username = username;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of();
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return Objects.equals(getId(), user.getId()) &&
              Objects.equals(getFirstName(), user.getFirstName()) &&
              Objects.equals(getLastName(), user.getLastName()) &&
              Objects.equals(getEmail(), user.getEmail()) &&
              Objects.equals(getUsername(), user.getUsername());
   }

   @Override
   public int hashCode() {
      return 31 * Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getUsername());
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("User - ");
      sb.append("id: ").append(id).append("\n");
      sb.append("firstName: ").append(firstName).append("\n");
      sb.append("lastName: ").append(lastName).append("\n");
      sb.append("email: ").append(email).append("\n");
      sb.append("username: ").append(username).append("\n");
      return sb.toString();
   }

   @Override
   public String getAuthority() {
      return "";
   }
}
