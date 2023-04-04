package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  @Id
  private Long id;

  @Column(nullable = false)
  private Long lobby;

  @Column(nullable = false, unique = true)
  private String username;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getLobby() {
    return lobby;
  }

  public void setLobby(Long lobby) {
    this.lobby = lobby;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


}
