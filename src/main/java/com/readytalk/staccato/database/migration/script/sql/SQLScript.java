package com.readytalk.staccato.database.migration.script.sql;

import java.net.URL;

import com.readytalk.staccato.database.migration.script.Script;

/**
 * Models a sql script.  SQL Scripts currently don't have a comparable implementation
 *
 * @author jhumphrey
 */
public class SQLScript implements Script<SQLScript> {

  private String filename;
  private URL url;

  @Override
  public String getFilename() {
    return filename;
  }

  @Override
  public URL getUrl() {
    return url;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  @Override
  public int compareTo(SQLScript sqlScript) {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SQLScript)) return false;

    SQLScript sqlScript = (SQLScript) o;

    if (!filename.equals(sqlScript.filename)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return filename.hashCode();
  }
}
