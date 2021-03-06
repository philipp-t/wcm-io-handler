/*-
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
;(function ($, ns, channel, document, window, undefined) {
  "use strict";

  var FileUploadExtension = function (config) {
    var self = this;
    self._element = config.element;
    self._$element = $(config.element);
    self._pathfield = config.pathfield;
    self._$pathfield = $(config.pathfield);
    self._bindEvents();

    // enable asset validation
    self._validate = new ns.MediaFormatValidate({
      pathfield: self._pathfield
    });
    // validate on load
    var assetPath = self._$pathfield.val();
    self._validate.validateMediaFormat(assetPath);
  };

  /**
   * Bind events to existing fileupload widget and pathfield widget.
   */
  FileUploadExtension.prototype._bindEvents = function () {
    var self = this;

    self._$element.find("[coral-fileupload-clear]").on("click", function (e) {
      self._$pathfield.val("");
      self._validate.validateMediaFormat(null);
    });

    self._$element.on("assetselected", function (event) {
      if (self._element.disabled) {
        return;
      }
      var assetPath = event.path;
      self._$pathfield.val(assetPath);
      self._validate.validateMediaFormat(assetPath);
    });

    self._$pathfield.on("change", function(event) {
      var assetPath = self._$pathfield.val();
      self._triggerAssetSelected(assetPath);
    });

    self._$pathfield.on("assetselected", function (event) {
      if (self._pathfield.disabled) {
        return;
      }
      var assetPath = event.path;
      self._triggerAssetSelected(assetPath);
    });

    self._element.on("coral-fileupload:load", function (event) {
      self._$pathfield.val("");
      self._validate.validateMediaFormat(null);
    });

  };

  /**
   * Trigger 'assetselected' event on the fileupload widget.
   */
  FileUploadExtension.prototype._triggerAssetSelected = function (assetPath)  {
    var self = this;
    var thumbnailUrl = assetPath + "/jcr:content/renditions/cq5dam.thumbnail.319.319.png";
    self._$element.trigger($.Event("assetselected", {
      path: assetPath,
      group: "media",
      mimetype: self._detectMimeType(assetPath),
      param: {},
      thumbnail: $("<img src='" + thumbnailUrl + "'>")
    }));
  };

  /**
   * Detect mime type from the file extension.
   */
  FileUploadExtension.prototype._detectMimeType = function (assetPath)  {
    var fileExtension = assetPath.substring(assetPath.lastIndexOf('.')+1, assetPath.length);
    if (fileExtension == "jpg" || fileExtension == "jpeg") {
      return "image/jpeg";
    }
    if (fileExtension == "png") {
      return "image/png";
    }
    if (fileExtension == "gif") {
      return "image/gif";
    }
    return null;
  };

  /**
   * Initializes file upload extension when dialog is loaded
   */
  channel.on("foundation-contentloaded", function (event) {
    $(event.target).find("coral-fileupload.cq-FileUpload").each(function() {
      var pathfield = $(this).closest(".coral-Form-fieldwrapper")
          .next("foundation-autocomplete.cq-FileUpload.wcm-io-handler-media-fileupload-pathfield").get(0);
      if (!pathfield) {
        // fallback for AEM 6.3 where the pathfield component is wrapped in an additionaly div
        pathfield = $(this).closest(".coral-Form-fieldwrapper")
          .next(".pathfield").find("foundation-autocomplete.cq-FileUpload.wcm-io-handler-media-fileupload-pathfield").get(0);
      }
      if (pathfield) {
        Coral.commons.ready(this, function (fileUpload) {
          // avoid double initialization if contentloaded event is fired twice e.g. in pageprops dialog
          if (!$(fileUpload).data("js-initialized")) {
            new FileUploadExtension({
              element: fileUpload,
              pathfield: pathfield
            });
            $(fileUpload).data("js-initialized", true);
          }
        });
      }
    });
  });

}(Granite.$, wcmio.handler.media, jQuery(document), document, this));
