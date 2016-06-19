/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-05-27 16:00:31 UTC)
 * on 2016-06-19 at 12:02:51 UTC 
 * Modify at your own risk.
 */

package com.appspot.iordanis_mobilezeit.wahlzeitApi.model;

/**
 * Model definition for Client.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the wahlzeitApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Client extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String accessRights;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private EmailAddress emailAddress;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String gender;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String language;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String nickName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean notifyAboutPraise;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String photoSize;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<PhotoId> praisedPhotoIds;

  static {
    // hack to force ProGuard to consider PhotoId used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(PhotoId.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String resourceId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<PhotoId> skippedPhotoIds;

  static {
    // hack to force ProGuard to consider PhotoId used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(PhotoId.class);
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAccessRights() {
    return accessRights;
  }

  /**
   * @param accessRights accessRights or {@code null} for none
   */
  public Client setAccessRights(java.lang.String accessRights) {
    this.accessRights = accessRights;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  /**
   * @param emailAddress emailAddress or {@code null} for none
   */
  public Client setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getGender() {
    return gender;
  }

  /**
   * @param gender gender or {@code null} for none
   */
  public Client setGender(java.lang.String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Client setId(java.lang.String id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLanguage() {
    return language;
  }

  /**
   * @param language language or {@code null} for none
   */
  public Client setLanguage(java.lang.String language) {
    this.language = language;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getNickName() {
    return nickName;
  }

  /**
   * @param nickName nickName or {@code null} for none
   */
  public Client setNickName(java.lang.String nickName) {
    this.nickName = nickName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getNotifyAboutPraise() {
    return notifyAboutPraise;
  }

  /**
   * @param notifyAboutPraise notifyAboutPraise or {@code null} for none
   */
  public Client setNotifyAboutPraise(java.lang.Boolean notifyAboutPraise) {
    this.notifyAboutPraise = notifyAboutPraise;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPhotoSize() {
    return photoSize;
  }

  /**
   * @param photoSize photoSize or {@code null} for none
   */
  public Client setPhotoSize(java.lang.String photoSize) {
    this.photoSize = photoSize;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<PhotoId> getPraisedPhotoIds() {
    return praisedPhotoIds;
  }

  /**
   * @param praisedPhotoIds praisedPhotoIds or {@code null} for none
   */
  public Client setPraisedPhotoIds(java.util.List<PhotoId> praisedPhotoIds) {
    this.praisedPhotoIds = praisedPhotoIds;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getResourceId() {
    return resourceId;
  }

  /**
   * @param resourceId resourceId or {@code null} for none
   */
  public Client setResourceId(java.lang.String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<PhotoId> getSkippedPhotoIds() {
    return skippedPhotoIds;
  }

  /**
   * @param skippedPhotoIds skippedPhotoIds or {@code null} for none
   */
  public Client setSkippedPhotoIds(java.util.List<PhotoId> skippedPhotoIds) {
    this.skippedPhotoIds = skippedPhotoIds;
    return this;
  }

  @Override
  public Client set(String fieldName, Object value) {
    return (Client) super.set(fieldName, value);
  }

  @Override
  public Client clone() {
    return (Client) super.clone();
  }

}
