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
 * on 2016-02-27 at 16:32:37 UTC 
 * Modify at your own risk.
 */

package com.appspot.iordanis_mobilezeit.wahlzeitApi.model;

/**
 * Model definition for Image.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the wahlzeitApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Image extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private BlobKey blobKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String format;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer height;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String imageData;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer width;

  /**
   * @return value or {@code null} for none
   */
  public BlobKey getBlobKey() {
    return blobKey;
  }

  /**
   * @param blobKey blobKey or {@code null} for none
   */
  public Image setBlobKey(BlobKey blobKey) {
    this.blobKey = blobKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFormat() {
    return format;
  }

  /**
   * @param format format or {@code null} for none
   */
  public Image setFormat(java.lang.String format) {
    this.format = format;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getHeight() {
    return height;
  }

  /**
   * @param height height or {@code null} for none
   */
  public Image setHeight(java.lang.Integer height) {
    this.height = height;
    return this;
  }

  /**
   * @see #decodeImageData()
   * @return value or {@code null} for none
   */
  public java.lang.String getImageData() {
    return imageData;
  }

  /**

   * @see #getImageData()
   * @return Base64 decoded value or {@code null} for none
   *
   * @since 1.14
   */
  public byte[] decodeImageData() {
    return com.google.api.client.util.Base64.decodeBase64(imageData);
  }

  /**
   * @see #encodeImageData()
   * @param imageData imageData or {@code null} for none
   */
  public Image setImageData(java.lang.String imageData) {
    this.imageData = imageData;
    return this;
  }

  /**

   * @see #setImageData()
   *
   * <p>
   * The value is encoded Base64 or {@code null} for none.
   * </p>
   *
   * @since 1.14
   */
  public Image encodeImageData(byte[] imageData) {
    this.imageData = com.google.api.client.util.Base64.encodeBase64URLSafeString(imageData);
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getWidth() {
    return width;
  }

  /**
   * @param width width or {@code null} for none
   */
  public Image setWidth(java.lang.Integer width) {
    this.width = width;
    return this;
  }

  @Override
  public Image set(String fieldName, Object value) {
    return (Image) super.set(fieldName, value);
  }

  @Override
  public Image clone() {
    return (Image) super.clone();
  }

}
