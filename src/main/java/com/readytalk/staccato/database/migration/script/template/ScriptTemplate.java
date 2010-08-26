package com.readytalk.staccato.database.migration.script.template;

import java.net.URL;

/**
 * Models a script template
 *
 * @author jhumphrey
 */
public class ScriptTemplate {

  private URL rawTemplate;
  private String templateContents;
  private String filename;
  private String classname;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public URL getRawTemplate() {
    return rawTemplate;
  }

  public void setRawTemplate(URL rawTemplate) {
    this.rawTemplate = rawTemplate;
  }

  public String getTemplateContents() {
    return templateContents;
  }

  public void setTemplateContents(String templateContents) {
    this.templateContents = templateContents;
  }

  public String getClassname() {
    return classname;
  }

  public void setClassname(String classname) {
    this.classname = classname;
  }

  @Override
  public String toString() {
    return templateContents;
  }
}