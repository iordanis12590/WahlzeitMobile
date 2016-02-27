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
 * (build: 2016-02-18 22:11:37 UTC)
 * on 2016-02-26 at 23:05:08 UTC 
 * Modify at your own risk.
 */

package com.appspot.iordanis_mobilezeit.wahlzeitApi.model;

/**
 * Model definition for User.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the wahlzeitApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class User extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String accessRights;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean confirmed;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long creationTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean dirty;

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
  private java.lang.String httpSessionId;

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
  private ModelConfig languageConfiguration;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Photo lastPraisedPhoto;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String nickName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer noOfPhotos;

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
  private java.util.List<Photo> photos;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Photo> photosReverseOrderedByPraise;

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
  private java.util.List<PhotoId> skippedPhotoIds;

  static {
    // hack to force ProGuard to consider PhotoId used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(PhotoId.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String status;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Image uploadedImage;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Photo userPhoto;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAccessRights() {
    return accessRights;
  }

  /**
   * @param accessRights accessRights or {@code null} for none
   */
  public User setAccessRights(java.lang.String accessRights) {
    this.accessRights = accessRights;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getConfirmed() {
    return confirmed;
  }

  /**
   * @param confirmed confirmed or {@code null} for none
   */
  public User setConfirmed(java.lang.Boolean confirmed) {
    this.confirmed = confirmed;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getCreationTime() {
    return creationTime;
  }

  /**
   * @param creationTime creationTime or {@code null} for none
   */
  public User setCreationTime(java.lang.Long creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getDirty() {
    return dirty;
  }

  /**
   * @param dirty dirty or {@code null} for none
   */
  public User setDirty(java.lang.Boolean dirty) {
    this.dirty = dirty;
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
  public User setEmailAddress(EmailAddress emailAddress) {
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
  public User setGender(java.lang.String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getHttpSessionId() {
    return httpSessionId;
  }

  /**
   * @param httpSessionId httpSessionId or {@code null} for none
   */
  public User setHttpSessionId(java.lang.String httpSessionId) {
    this.httpSessionId = httpSessionId;
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
  public User setId(java.lang.String id) {
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
  public User setLanguage(java.lang.String language) {
    this.language = language;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public ModelConfig getLanguageConfiguration() {
    return languageConfiguration;
  }

  /**
   * @param languageConfiguration languageConfiguration or {@code null} for none
   */
  public User setLanguageConfiguration(ModelConfig languageConfiguration) {
    this.languageConfiguration = languageConfiguration;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Photo getLastPraisedPhoto() {
    return lastPraisedPhoto;
  }

  /**
   * @param lastPraisedPhoto lastPraisedPhoto or {@code null} for none
   */
  public User setLastPraisedPhoto(Photo lastPraisedPhoto) {
    this.lastPraisedPhoto = lastPraisedPhoto;
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
  public User setNickName(java.lang.String nickName) {
    this.nickName = nickName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNoOfPhotos() {
    return noOfPhotos;
  }

  /**
   * @param noOfPhotos noOfPhotos or {@code null} for none
   */
  public User setNoOfPhotos(java.lang.Integer noOfPhotos) {
    this.noOfPhotos = noOfPhotos;
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
  public User setNotifyAboutPraise(java.lang.Boolean notifyAboutPraise) {
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
  public User setPhotoSize(java.lang.String photoSize) {
    this.photoSize = photoSize;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Photo> getPhotos() {
    return photos;
  }

  /**
   * @param photos photos or {@code null} for none
   */
  public User setPhotos(java.util.List<Photo> photos) {
    this.photos = photos;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Photo> getPhotosReverseOrderedByPraise() {
    return photosReverseOrderedByPraise;
  }

  /**
   * @param photosReverseOrderedByPraise photosReverseOrderedByPraise or {@code null} for none
   */
  public User setPhotosReverseOrderedByPraise(java.util.List<Photo> photosReverseOrderedByPraise) {
    this.photosReverseOrderedByPraise = photosReverseOrderedByPraise;
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
  public User setPraisedPhotoIds(java.util.List<PhotoId> praisedPhotoIds) {
    this.praisedPhotoIds = praisedPhotoIds;
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
  public User setSkippedPhotoIds(java.util.List<PhotoId> skippedPhotoIds) {
    this.skippedPhotoIds = skippedPhotoIds;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStatus() {
    return status;
  }

  /**
   * @param status status or {@code null} for none
   */
  public User setStatus(java.lang.String status) {
    this.status = status;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Image getUploadedImage() {
    return uploadedImage;
  }

  /**
   * @param uploadedImage uploadedImage or {@code null} for none
   */
  public User setUploadedImage(Image uploadedImage) {
    this.uploadedImage = uploadedImage;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Photo getUserPhoto() {
    return userPhoto;
  }

  /**
   * @param userPhoto userPhoto or {@code null} for none
   */
  public User setUserPhoto(Photo userPhoto) {
    this.userPhoto = userPhoto;
    return this;
  }

  @Override
  public User set(String fieldName, Object value) {
    return (User) super.set(fieldName, value);
  }

  @Override
  public User clone() {
    return (User) super.clone();
  }

}
