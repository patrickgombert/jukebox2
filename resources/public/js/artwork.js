(function() {
  var Artwork;
  Artwork = (function() {
    function Artwork() {
      this.album = void 0;
      this.artist = void 0;
      this.rendered = false;
      $('#current_track').delegate('.album-cover', 'artwork.render', this.render);
      this.load();
      $('#current_track').bind('track.updated', this.load);
    }
    Artwork.prototype.load = function() {
      var $covers;
      $covers = $('.album-cover');
      return $covers.each(function() {
        var $cover;
        $cover = $(this);
        this.album = $cover.attr('data-album');
        this.artist = $cover.attr('data-artist');
        return $.get('http://ws.audioscrobbler.com/2.0/', {
          "method": "album.getInfo",
          "api_key": "809bf298f1f11c57fbb680b1befdf476",
          "album": this.album,
          "artist": this.artist
        }, function(data) {
          return $cover.trigger('artwork.render', [data]);
        });
      });
    };
    Artwork.prototype.render = function(e, info) {
      var img, size, src, wrap;
      size = $(this).attr('data-thumbnail');
      src = $(info).find("image[size=" + size + "]").text();
      if (src === '') {
        src = '/img/no_art_lrg.png';
      }
      if (this.rendered) {
        $('.thumbnail').attr('src', src);
      } else {
        wrap = $('<a />', {
          'href': '#'
        });
        img = $('<img>', {
          'class': 'thumbnail',
          'src': src
        });
        wrap.append(img);
        $(this).append(wrap);
      }
      return this.rendered = true;
    };
    return Artwork;
  })();
  $(function() {
    return new Artwork;
  });
}).call(this);
