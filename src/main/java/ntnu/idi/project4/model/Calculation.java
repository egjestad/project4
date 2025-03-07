package ntnu.idi.project4.model;

//TODO: Change class to right data
public class Calculation {
  private long id;
  private String title;
  private String description;
  private boolean published;

  public Calculation() {

  }

  public Calculation(String title, String description, boolean published) {
    this.title = title;
    this.description = description;
    this.published = published;
  }

  public Calculation(long id, String title, String description, boolean published) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.published = published;
  }
  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean isPublished) {
    this.published = isPublished;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
  }
}

