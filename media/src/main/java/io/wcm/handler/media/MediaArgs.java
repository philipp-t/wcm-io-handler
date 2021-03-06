/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.handler.media;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.media.format.MediaFormat;
import io.wcm.handler.media.markup.DragDropSupport;
import io.wcm.handler.media.markup.IPERatioCustomize;
import io.wcm.handler.url.UrlMode;
import io.wcm.wcm.commons.util.ToStringStyle;

/**
 * Holds parameters to influence the media resolving process.
 */
@ProviderType
public final class MediaArgs implements Cloneable {

  private MediaFormat[] mediaFormats;
  private String[] mediaFormatNames;
  private boolean mediaFormatsMandatory;
  private boolean autoCrop;
  private String[] fileExtensions;
  private UrlMode urlMode;
  private long fixedWidth;
  private long fixedHeight;
  private boolean download;
  private boolean contentDispositionAttachment;
  private String altText;
  private boolean dummyImage = true;
  private String dummyImageUrl;
  private boolean includeAssetThumbnails;
  private ImageSizes imageSizes;
  private PictureSource[] pictureSourceSets;
  private DragDropSupport dragDropSupport = DragDropSupport.AUTO;
  private IPERatioCustomize ipeRatioCustomize = IPERatioCustomize.AUTO;
  private ValueMap properties;

  /**
   * Default constructor
   */
  public MediaArgs() {
    // default constructor
  }

  /**
   * @param mediaFormats Media formats
   */
  public MediaArgs(MediaFormat... mediaFormats) {
    mediaFormats(mediaFormats);
  }

  /**
   * @param mediaFormatNames Media format names
   */
  public MediaArgs(String... mediaFormatNames) {
    mediaFormatNames(mediaFormatNames);
  }

  /**
   * Returns list of media formats to resolve to. If {@link #isMediaFormatsMandatory()} is false,
   * the first rendition that matches any of the given media format is returned. If it is set to true,
   * for each media format given a rendition has to be resolved and returned. If not all renditions
   * could be resolved the media is marked as invalid (but the partial resolved renditions are returned anyway).
   * @return Media formats
   */
  public MediaFormat[] getMediaFormats() {
    return this.mediaFormats;
  }

  /**
   * Sets list of media formats to resolve to.
   * @param values Media formats
   * @return this
   */
  public @NotNull MediaArgs mediaFormats(MediaFormat... values) {
    if (values == null || values.length == 0) {
      this.mediaFormats = null;
    }
    else {
      this.mediaFormats = values;
    }
    return this;
  }

  /**
   * Sets list of media formats to resolve to.
   * Additionally {@link #isMediaFormatsMandatory()} is set to true.
   * @param values Media formats
   * @return this
   */
  public @NotNull MediaArgs mandatoryMediaFormats(MediaFormat... values) {
    mediaFormats(values);
    mediaFormatsMandatory(true);
    return this;
  }

  /**
   * Sets a single media format to resolve to.
   * @param value Media format
   * @return this
   */
  public @NotNull MediaArgs mediaFormat(MediaFormat value) {
    if (value == null) {
      this.mediaFormats = null;
    }
    else {
      this.mediaFormats = new MediaFormat[] {
          value
      };
    }
    return this;
  }

  /**
   * If set to true, media handler never returns a dummy image. Otherwise this can happen in edit mode.
   * @return Resolving of all media formats is mandatory.
   */
  public boolean isMediaFormatsMandatory() {
    return this.mediaFormatsMandatory;
  }

  /**
   * If set to true, the media handler enforces the resolution of the whole list of given
   * media formats. The resolution fails if any of the media formats does not match.
   * @param value Resolving of all media formats is mandatory.
   * @return this
   */
  public @NotNull MediaArgs mediaFormatsMandatory(boolean value) {
    this.mediaFormatsMandatory = value;
    return this;
  }

  /**
   * Returns list of media formats to resolve to. See {@link #getMediaFormatNames()} for details.
   * @return Media format names
   */
  public String[] getMediaFormatNames() {
    return this.mediaFormatNames;
  }

  /**
   * Sets list of media formats to resolve to.
   * @param values Media format names.
   * @return this
   */
  public @NotNull MediaArgs mediaFormatNames(String... values) {
    if (values == null || values.length == 0) {
      this.mediaFormatNames = null;
    }
    else {
      this.mediaFormatNames = values;
    }
    return this;
  }

  /**
   * Sets list of media formats to resolve to.
   * Additionally {@link #isMediaFormatsMandatory()} is set to true.
   * @param values Media format names.
   * @return this
   */
  public @NotNull MediaArgs mandatoryMediaFormatNames(String... values) {
    mediaFormatNames(values);
    mediaFormatsMandatory(true);
    return this;
  }

  /**
   * Sets a single media format to resolve to.
   * @param value Media format name
   * @return this
   */
  public @NotNull MediaArgs mediaFormatName(String value) {
    if (value == null) {
      this.mediaFormatNames = null;
    }
    else {
      this.mediaFormatNames = new String[] {
          value
      };
    }
    return this;
  }

  /**
   * @return Enables "auto-cropping" mode. If no matching rendition is found
   *         it is tried to generate one by automatically cropping another one.
   */
  public boolean isAutoCrop() {
    return this.autoCrop;
  }

  /**
   * @param value Enables "auto-cropping" mode. If no matching rendition is found
   *          it is tried to generate one by automatically cropping another one.
   * @return this
   */
  public @NotNull MediaArgs autoCrop(boolean value) {
    this.autoCrop = value;
    return this;
  }

  /**
   * @return File extensions
   */
  public String[] getFileExtensions() {
    return this.fileExtensions;
  }

  /**
   * @param values File extensions
   * @return this
   */
  public @NotNull MediaArgs fileExtensions(String... values) {
    if (values == null || values.length == 0) {
      this.fileExtensions = null;
    }
    else {
      this.fileExtensions = values;
    }
    return this;
  }

  /**
   * @param value File extension
   * @return this
   */
  public @NotNull MediaArgs fileExtension(String value) {
    if (value == null) {
      this.fileExtensions = null;
    }
    else {
      this.fileExtensions = new String[] {
          value
      };
    }
    return this;
  }

  /**
   * @return URL mode
   */
  public UrlMode getUrlMode() {
    return this.urlMode;
  }

  /**
   * @param value URS mode
   * @return this
   */
  public @NotNull MediaArgs urlMode(UrlMode value) {
    this.urlMode = value;
    return this;
  }

  /**
   * Use fixed width instead of width from media format or original image
   * @return Fixed width
   */
  public long getFixedWidth() {
    return this.fixedWidth;
  }

  /**
   * Use fixed width instead of width from media format or original image
   * @param value Fixed width
   * @return this
   */
  public @NotNull MediaArgs fixedWidth(long value) {
    this.fixedWidth = value;
    return this;
  }

  /**
   * Use fixed height instead of width from media format or original image
   * @return Fixed height
   */
  public long getFixedHeight() {
    return this.fixedHeight;
  }

  /**
   * Use fixed height instead of width from media format or original image
   * @param value Fixed height
   * @return this
   */
  public @NotNull MediaArgs fixedHeight(long value) {
    this.fixedHeight = value;
    return this;
  }

  /**
   * Use fixed dimensions instead of width from media format or original image
   * @param widthValue Fixed width
   * @param heightValue Fixed height
   * @return this
   */
  public @NotNull MediaArgs fixedDimension(long widthValue, long heightValue) {
    this.fixedWidth = widthValue;
    this.fixedHeight = heightValue;
    return this;
  }

  /**
   * @return Accept only media formats that have the download flag set.
   */
  public boolean isDownload() {
    return this.download;
  }

  /**
   * @param value Accept only media formats that have the download flag set.
   * @return this
   */
  public @NotNull MediaArgs download(boolean value) {
    this.download = value;
    return this;
  }

  /**
   * @return Whether to set a "Content-Disposition" header to "attachment" for forcing a "Save as" dialog on the client
   */
  public boolean isContentDispositionAttachment() {
    return this.contentDispositionAttachment;
  }

  /**
   * @param value Whether to set a "Content-Disposition" header to "attachment" for forcing a "Save as" dialog on the
   *          client
   * @return this
   */
  public @NotNull MediaArgs contentDispositionAttachment(boolean value) {
    this.contentDispositionAttachment = value;
    return this;
  }

  /**
   * @return The custom alternative text that is to be used instead of the one defined in the the media lib item
   */
  public String getAltText() {
    return this.altText;
  }

  /**
   * Allows to specify a custom alternative text that is to be used instead of the one defined in the the media lib item
   * @param value Custom alternative text. If null or empty, the default alt text from media library is used.
   * @return this
   */
  public @NotNull MediaArgs altText(String value) {
    this.altText = value;
    return this;
  }

  /**
   * @return If set to true, media handler never returns a dummy image. Otherwise this can happen in edit mode.
   */
  public boolean isDummyImage() {
    return this.dummyImage;
  }

  /**
   * @param value If set to false, media handler never returns a dummy image. Otherwise this can happen in edit mode.
   * @return this
   */
  public @NotNull MediaArgs dummyImage(boolean value) {
    this.dummyImage = value;
    return this;
  }

  /**
   * @return Url of custom dummy image. If null default dummy image is used.
   */
  public String getDummyImageUrl() {
    return this.dummyImageUrl;
  }

  /**
   * @param value Url of custom dummy image. If null default dummy image is used.
   * @return this
   */
  public @NotNull MediaArgs dummyImageUrl(String value) {
    this.dummyImageUrl = value;
    return this;
  }

  /**
   * @return If set to true, thumbnail generated by the DAM asset workflows (with cq5dam.thumbnail prefix) are taken
   *         into account as well when trying to resolve the media request.
   */
  public boolean isIncludeAssetThumbnails() {
    return this.includeAssetThumbnails;
  }

  /**
   * @param value If set to true, thumbnail generated by the DAM asset workflows (with cq5dam.thumbnail prefix) are
   *          taken into account as well when trying to resolve the media request.
   * @return this
   */
  public @NotNull MediaArgs includeAssetThumbnails(boolean value) {
    this.includeAssetThumbnails = value;
    return this;
  }

  /**
   * @return Image sizes for responsive image handling
   */
  public ImageSizes getImageSizes() {
    return this.imageSizes;
  }

  /**
   * @param value Image sizes for responsive image handling
   * @return this
   */
  public @NotNull MediaArgs imageSizes(ImageSizes value) {
    this.imageSizes = value;
    return this;
  }

  /**
   * @return Picture sources for responsive image handling
   */
  public PictureSource[] getPictureSources() {
    return this.pictureSourceSets;
  }

  /**
   * @param value Picture sources for responsive image handling
   * @return this
   */
  public @NotNull MediaArgs pictureSources(PictureSource[] value) {
    this.pictureSourceSets = value;
    return this;
  }

  /**
   * Drag&amp;Drop support for media builder.
   * @return Drag&amp;Drop support
   */
  public DragDropSupport getDragDropSupport() {
    return this.dragDropSupport;
  }

  /**
   * Drag&amp;Drop support for media builder.
   * @param value Drag&amp;Drop support
   * @return this
   */
  public @NotNull MediaArgs dragDropSupport(DragDropSupport value) {
    if (value == null) {
      throw new IllegalArgumentException("No null value allowed for drag&drop support.");
    }
    this.dragDropSupport = value;
    return this;
  }

  /**
   * @return Whether to set customized list of IPE cropping ratios.
   */
  public IPERatioCustomize getIPERatioCustomize() {
    return this.ipeRatioCustomize;
  }

  /**
   * @param value Whether to set customized list of IPE cropping ratios.
   * @return this
   */
  public @NotNull MediaArgs ipeRatioCustomize(IPERatioCustomize value) {
    this.ipeRatioCustomize = value;
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param map Property map. Is merged with properties already set.
   * @return this
   */
  public @NotNull MediaArgs properties(Map<String, Object> map) {
    if (map == null) {
      throw new IllegalArgumentException("Map argument must not be null.");
    }
    getProperties().putAll(map);
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @param key Property key
   * @param value Property value
   * @return this
   */
  public @NotNull MediaArgs property(String key, Object value) {
    if (key == null) {
      throw new IllegalArgumentException("Key argument must not be null.");
    }
    getProperties().put(key, value);
    return this;
  }

  /**
   * Custom properties that my be used by application-specific markup builders or processors.
   * @return Value map
   */
  public ValueMap getProperties() {
    if (this.properties == null) {
      this.properties = new ValueMapDecorator(new HashMap<String, Object>());
    }
    return this.properties;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_OMIT_NULL_STYLE);
  }

  /**
   * Custom clone-method for {@link MediaArgs}
   * @return the cloned {@link MediaArgs}
   */
  // CHECKSTYLE:OFF
  @Override
  public MediaArgs clone() { //NOPMD
    // CHECKSTYLE:ON
    MediaArgs clone = new MediaArgs();

    clone.mediaFormats = ArrayUtils.clone(this.mediaFormats);
    clone.mediaFormatNames = ArrayUtils.clone(this.mediaFormatNames);
    clone.mediaFormatsMandatory = this.mediaFormatsMandatory;
    clone.autoCrop = this.autoCrop;
    clone.fileExtensions = ArrayUtils.clone(this.fileExtensions);
    clone.urlMode = this.urlMode;
    clone.fixedWidth = this.fixedWidth;
    clone.fixedHeight = this.fixedHeight;
    clone.download = this.download;
    clone.contentDispositionAttachment = this.contentDispositionAttachment;
    clone.altText = this.altText;
    clone.dummyImage = this.dummyImage;
    clone.dummyImageUrl = this.dummyImageUrl;
    clone.includeAssetThumbnails = this.includeAssetThumbnails;
    clone.imageSizes = this.imageSizes;
    clone.pictureSourceSets = ArrayUtils.clone(this.pictureSourceSets);
    clone.dragDropSupport = this.dragDropSupport;
    clone.ipeRatioCustomize = this.ipeRatioCustomize;
    if (this.properties != null) {
      clone.properties = new ValueMapDecorator(new HashMap<String, Object>(this.properties));
    }

    return clone;
  }


  /**
   * Image sizes for responsive image handling.
   */
  @ProviderType
  public static final class ImageSizes {

    private final @NotNull String sizes;
    private final long @NotNull [] widths;

    /**
     * @param sizes A <a href="http://w3c.github.io/html/semantics-embedded-content.html#valid-source-size-list">valid
     *          source size list</a>
     * @param widths Widths for the renditions in the <code>srcset</code> attribute.
     */
    public ImageSizes(@NotNull String sizes, long @NotNull... widths) {
      this.sizes = sizes;
      this.widths = widths;
    }

    /**
     * @return A <a href="http://w3c.github.io/html/semantics-embedded-content.html#valid-source-size-list">valid
     *         source size list</a>
     */
    public @NotNull String getSizes() {
      return this.sizes;
    }

    /**
     * @return Widths for the renditions in the <code>srcset</code> attribute.
     */
    public long @NotNull [] getWidths() {
      return this.widths;
    }

    @Override
    public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }

  }

  /**
   * Picture source for responsive image handling.
   */
  @ProviderType
  public static final class PictureSource {

    private final @NotNull MediaFormat mediaFormat;
    private final @Nullable String media;
    private final long @NotNull [] widths;

    /**
     * @param mediaFormat Media format
     * @param media A <a href="http://w3c.github.io/html/infrastructure.html#valid-media-query-list">valid media query
     *          list</a>
     * @param widths Widths for the renditions in the <code>srcset</code> attribute.
     */
    public PictureSource(@NotNull MediaFormat mediaFormat, @Nullable String media, long @NotNull... widths) {
      this.mediaFormat = mediaFormat;
      this.media = media;
      this.widths = widths;
    }

    /**
     * @return Media format
     */
    public @NotNull MediaFormat getMediaFormat() {
      return this.mediaFormat;
    }

    /**
     * @return A <a href="http://w3c.github.io/html/infrastructure.html#valid-media-query-list">valid media query
     *         list</a>
     */
    public @Nullable String getMedia() {
      return this.media;
    }

    /**
     * @return Widths for the renditions in the <code>srcset</code> attribute.
     */
    public long @NotNull [] getWidths() {
      return this.widths;
    }

    @Override
    public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }

  }

}
