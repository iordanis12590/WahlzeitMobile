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
 * on 2016-07-07 at 08:58:51 UTC 
 * Modify at your own risk.
 */

package com.appspot.iordanis_mobilezeit.wahlzeitApi.model;

/**
 * Model definition for Photo.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the wahlzeitApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Photo extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String blobImage;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long creationTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private PhotoId id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String idAsString;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String imagesURI;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private EmailAddress ownerEmailAddress;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ownerId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ownerLanguage;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ownerNickName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean ownerNotifyAboutPraise;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double praise;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String praisingClientId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer rating;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ratingURI;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String resourceId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String status;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Tags tags;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String upNavigationUris;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean visible;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getBlobImage() {
    return blobImage;
  }

  /**
   * @param blobImage blobImage or {@code null} for none
   */
  public Photo setBlobImage(java.lang.String blobImage) {
    this.blobImage = blobImage;
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
  public Photo setCreationTime(java.lang.Long creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public PhotoId getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Photo setId(PhotoId id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getIdAsString() {
    return idAsString;
  }

  /**
   * @param idAsString idAsString or {@code null} for none
   */
  public Photo setIdAsString(java.lang.String idAsString) {
    this.idAsString = idAsString;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getImagesURI() {
    return imagesURI;
  }

  /**
   * @param imagesURI imagesURI or {@code null} for none
   */
  public Photo setImagesURI(java.lang.String imagesURI) {
    this.imagesURI = imagesURI;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public EmailAddress getOwnerEmailAddress() {
    return ownerEmailAddress;
  }

  /**
   * @param ownerEmailAddress ownerEmailAddress or {@code null} for none
   */
  public Photo setOwnerEmailAddress(EmailAddress ownerEmailAddress) {
    this.ownerEmailAddress = ownerEmailAddress;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwnerId() {
    return ownerId;
  }

  /**
   * @param ownerId ownerId or {@code null} for none
   */
  public Photo setOwnerId(java.lang.String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwnerLanguage() {
    return ownerLanguage;
  }

  /**
   * @param ownerLanguage ownerLanguage or {@code null} for none
   */
  public Photo setOwnerLanguage(java.lang.String ownerLanguage) {
    this.ownerLanguage = ownerLanguage;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwnerNickName() {
    return ownerNickName;
  }

  /**
   * @param ownerNickName ownerNickName or {@code null} for none
   */
  public Photo setOwnerNickName(java.lang.String ownerNickName) {
    this.ownerNickName = ownerNickName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getOwnerNotifyAboutPraise() {
    return ownerNotifyAboutPraise;
  }

  /**
   * @param ownerNotifyAboutPraise ownerNotifyAboutPraise or {@code null} for none
   */
  public Photo setOwnerNotifyAboutPraise(java.lang.Boolean ownerNotifyAboutPraise) {
    this.ownerNotifyAboutPraise = ownerNotifyAboutPraise;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getPraise() {
    return praise;
  }

  /**
   * @param praise praise or {@code null} for none
   */
  public Photo setPraise(java.lang.Double praise) {
    this.praise = praise;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPraisingClientId() {
    return praisingClientId;
  }

  /**
   * @param praisingClientId praisingClientId or {@code null} for none
   */
  public Photo setPraisingClientId(java.lang.String praisingClientId) {
    this.praisingClientId = praisingClientId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getRating() {
    return rating;
  }

  /**
   * @param rating rating or {@code null} for none
   */
  public Photo setRating(java.lang.Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getRatingURI() {
    return ratingURI;
  }

  /**
   * @param ratingURI ratingURI or {@code null} for none
   */
  public Photo setRatingURI(java.lang.String ratingURI) {
    this.ratingURI = ratingURI;
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
  public Photo setResourceId(java.lang.String resourceId) {
    this.resourceId = resourceId;
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
  public Photo setStatus(java.lang.String status) {
    this.status = status;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Tags getTags() {
    return tags;
  }

  /**
   * @param tags tags or {@code null} for none
   */
  public Photo setTags(Tags tags) {
    this.tags = tags;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUpNavigationUris() {
    return upNavigationUris;
  }

  /**
   * @param upNavigationUris upNavigationUris or {@code null} for none
   */
  public Photo setUpNavigationUris(java.lang.String upNavigationUris) {
    this.upNavigationUris = upNavigationUris;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getVisible() {
    return visible;
  }

  /**
   * @param visible visible or {@code null} for none
   */
  public Photo setVisible(java.lang.Boolean visible) {
    this.visible = visible;
    return this;
  }

  @Override
  public Photo set(String fieldName, Object value) {
    return (Photo) super.set(fieldName, value);
  }

  @Override
  public Photo clone() {
    return (Photo) super.clone();
  }

}
