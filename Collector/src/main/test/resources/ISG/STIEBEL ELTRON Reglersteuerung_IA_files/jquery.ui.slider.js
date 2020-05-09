/*
 * jQuery UI Slider 1.8.14
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Slider
 *
 * Depends:
 *	jquery.ui.core.js
 *	jquery.ui.mouse.js
 *	jquery.ui.widget.js
 */
(function( $, undefined ) {

// number of pages in a slider
// (how many times can you page up/down to go through the whole range)
var numPages = 5;

$.widget( "ui.slider", $.ui.mouse, {

	widgetEventPrefix: "slide",

	options: {
		animate: false,
		distance: 0,
		max: 100,
		min: 0,
		orientation: "horizontal",
		range: false,
		step: 1,
		value: 0,
		values: null,
    
    // OTTO -->
    ranges: false,      // mehrere Ranges
    pushSlider: false,  // Nachbarslider mitverschieben
    slideGroup: false,  // gruppe per range verschieben
    dblchange: false,   // doppelklick aktionen ( hinzufügen, entfernen )
    dependency: "",     // zweiter externer schieber für die anzahl der ranges
    puffer: [],         // values ausgangspuffer
    mousePuffer: 0,     // nur für *slideGroup
    ids: [],            // auf der Webseite zu aendernde input[hidden] felder zum saven
    defaultValue: 0,    // Rückgabewert für aus
    touchdblclicktime: 400
    // <-- OTTO
	},

	_create: function() {
		var self = this,
			o = this.options;
      
      // OTTO -->
      if( o.ranges == true && !( o.values % 2 ) && o.values.length >= 4 ){
        sortV = function(){
          for( var i = 0, ii = 2; ii < o.values.length; i += 2, ii += 2 ){
            if( o.values[i] > o.values[ii] && o.values[i+1] > o.values[ii+1] ){
              o.values.splice( ii+2, 0, o.values[i], o.values[i+1]);
              o.values.splice( i, 2 );
              sortV();
              break;
            }else if( o.values[i] < o.values[ii] && o.values[i+1] >= o.values[ii] ){
              o.values.splice( i+1, 2 );
              sortV();
              break;
            }else if( o.values[i] > o.values[ii] && o.values[i+1] <= o.values[ii+1] ){
              o.values.splice( i, 2 );
              sortV();
              break;
            }else if( o.values[i] <= o.values[ii] && o.values[i+1] >= o.values[ii+1] ){
              o.values.splice( ii, 2 );
              sortV();
              break;
            }
          }
        }
        sortV();
        
        sortP = function(){
          for( var i = 0, ii = 2; ii < o.puffer.length; i += 2, ii += 2 ){
            if( o.puffer[i] > o.puffer[ii] && o.puffer[i+1] > o.puffer[ii+1] ){
              o.puffer.splice( ii+2, 0, o.puffer[i], o.puffer[i+1]);
              o.puffer.splice( i, 2 );
              sortP();
              break;
            }else if( o.puffer[i] < o.puffer[ii] && o.puffer[i+1] >= o.puffer[ii] ){
              o.puffer.splice( i+1, 2 );
              sortP();
              break;
            }else if( o.puffer[i] > o.puffer[ii] && o.puffer[i+1] <= o.puffer[ii+1] ){
              o.puffer.splice( i, 2 );
              sortP();
              break;
            }else if( o.puffer[i] <= o.puffer[ii] && o.puffer[i+1] >= o.puffer[ii+1] ){
              o.puffer.splice( ii, 2 );
              sortP();
              break;
            }
          }
        }
        sortP();
      }
      // <-- OTTO
      
			existingHandles = this.element.find( ".ui-slider-handle" ).addClass( "ui-state-default ui-corner-all" ),
			handle = "<a class='ui-slider-handle ui-state-default ui-corner-all' href='#'></a>",
			handleCount = ( o.values && o.values.length ) || 1,
			handles = [],
      
      // OTTO -->
      existingRanges = this.element.find( ".ui-slider-range" ).addClass( "ui-widget-header" ),
      range = "<div class='ui-slider-range ui-widget-header"+ ( ( o.range === "min" || o.range === "max" ) ? " ui-slider-range-" + o.range : "" ) +"' href='#'></div>",
      rangeCount = ( ( o.values && o.values.length ) ? ( o.values.length / 2 ) : 1 ),
      ranges = [];
      
      if( o.ranges ){
        handleCount = o.values.length;
        rangeCount = (o.values.length / 2);
      }
      // <-- OTTO

		this._keySliding = false;
		this._mouseSliding = false;
		this._animateOff = true;
		this._handleIndex = null;
		this._detectOrientation();
		this._mouseInit();

		this.element
			.addClass( "ui-slider" +
				" ui-slider-" + this.orientation +
				" ui-widget" +
				" ui-widget-content" +
				" ui-corner-all" +
				( o.disabled ? " ui-slider-disabled ui-disabled" : "" ) );
        
    this.element.unbind( 'touchstart' );
    this.element.bind( 'touchstart' , function( event ){
      if( self._checkForDoubleTap( this ) ){
      
        var S = $( this ),
            I = 0,
            E = event.originalEvent,
            left = S.offset().left,
            right = left + S.width(),
            normValue = o.min + ( E.touches.item(0).clientX-left ) / ( right - left ) * ( o.max - o.min );
      
        self._createGroup( normValue );
        self._isChange();
      }
      return false;
      //if( event.originalEvent.touches.item(0) && event.originalEvent.touches.item(1) ){
    });

		this.range = $([]);

		if ( o.range ) {
			if ( o.range === true ) {
				if ( !o.values ) {
					o.values = [ this._valueMin(), this._valueMin() ];
				}
        if( !o.ranges ){ // OTTO
  				if ( o.values.length && o.values.length !== 2 ) {
  					o.values = [ o.values[0], o.values[0] ];
  				}
        }
			}
      
      /* OTTO -->
      
			this.range = $( "<div></div>" )
				.appendTo( this.element )
				.addClass( "ui-slider-range" +
				// note: this isn't the most fittingly semantic framework class for this element,
				// but worked best visually with a variety of themes
				" ui-widget-header" + 
				( ( o.range === "min" || o.range === "max" ) ? " ui-slider-range-" + o.range : "" ) );
        
      --> OTTO */
      
		}
    
    // OTTO -->
    for ( var i = existingRanges.length; i < rangeCount; i += 1 ) {
			ranges.push( range );
		}
    // <-- OTTO

		for ( var i = existingHandles.length; i < handleCount; i += 1 ) {
			handles.push( handle );
		}

		this.handles = existingHandles.add( $( handles.join( "" ) ).appendTo( self.element ) );
    this.ranges = existingRanges.add( $( ranges.join( "" ) ).appendTo( self.element ) ); // OTTO

		this.handle = this.handles.eq( 0 );
    this.range = this.ranges.eq( 0 ); // OTTO
    
		this.handles.add( this.range ).filter( "a" )
			.click(function( event ) {
				event.preventDefault();
			})
			.hover(function() {
				if ( !o.disabled ) {
					$( this ).addClass( "ui-state-hover" );
				}
			}, function() {
				$( this ).removeClass( "ui-state-hover" );
			})
			.focus(function() {
				if ( !o.disabled ) {
					$( ".ui-slider .ui-state-focus" ).removeClass( "ui-state-focus" );
					$( this ).addClass( "ui-state-focus" );
				} else {
					$( this ).blur();
				}
			})
			.blur(function() {
				$( this ).removeClass( "ui-state-focus" );
			})
      // OTTO -->
      .unbind( 'touchmove' )
      .bind( 'touchmove' , function( event ){
      
        var S = $( self ),
            T = $( this ),
            I = 0,
            E = event.originalEvent,
            left = T.parent().offset().left,
            right = left + T.parent().width(),
            newVal = parseInt( o.min + ( E.touches.item(0).clientX-left ) / ( right - left ) * ( o.max - o.min ) );
          
        self.handles.each( function( i ){
          if( $( this ).css( "left" ) == T.css( "left" ) ){
            I = i;
          }
        });
        
        document.ontouchmove = function(e){ e.preventDefault(); }

        self._slide( event, I, newVal );
        this._isChange();
      })
      .bind( "touchend", function(){
        document.ontouchmove = function(e){ return e; }
      });
      // <-- OTTO
    

		this.handles.each(function( i ) {
			$( this ).data( "index.ui-slider-handle", i );
		});
    
    // OTTO -->
    this.ranges.filter( "div" )
			.click(function( event ) {
				event.preventDefault();
			})
			.hover(function() {
				if ( !o.disabled ) {
					$( this ).addClass( "ui-state-hover" );
				}
			}, function() {
				$( this ).removeClass( "ui-state-hover" );
			})
			.focus(function() {
				if ( !o.disabled ) {
					$( ".ui-range .ui-state-focus" ).removeClass( "ui-state-focus" );
					$( this ).addClass( "ui-state-focus" );
				} else {
					$( this ).blur();
				}
			})
			.blur(function() {
				$( this ).removeClass( "ui-state-focus" );
			})
      .unbind( 'touchstart' )
      .bind( 'touchstart' , function( event ){
      
        event.stopPropagation();
        
        var indexRange;
        var thisRange = $(this);
        self.ranges.each( function( i ){
          if( $(this).css( "left" ) == thisRange.css( "left" ) ){
            indexRange = i;
          }
        });
        
        if( self._checkForDoubleTap( self.ranges[indexRange] ) ){
          self._deleteGroup( indexRange );
          self._isChange();
        }
      
        if( o.slideGroup === true ){ 
          var T = $( this ),
            I = 0,
            E = event.originalEvent,
            left = T.parent().offset().left,
            right = left + T.parent().width(),
            normValue = parseInt( o.min + ( E.touches.item(0).clientX-left ) / ( right - left ) * ( o.max - o.min ) );
      
          o.mousePuffer = normValue;
        }
      })
      .unbind( 'touchmove' )
      .bind( 'touchmove' , function( event ){
      
        var T = $( this ),
            I = 0,
            E = event.originalEvent,
            left = T.parent().offset().left,
            right = left + T.parent().width(),
            normValue = parseInt( o.min + ( E.touches.item(0).clientX-left ) / ( right - left ) * ( o.max - o.min ) ),
    		    distance = self._valueMax() - self._valueMin() + 1;
            
    		self.handles.each(function( i ) {
    			var thisDistance = Math.abs( normValue - self.values(i) );
    			if ( distance > thisDistance ) {
    				distance = thisDistance;
    				I = i;
    			}
    		});
        
        document.ontouchmove = function(e){ e.preventDefault(); }
        
        self._slideGroup( event, I, normValue );
        this._isChange();
      })
      .bind( "touchend", function(){
        document.ontouchmove = function(e){ return e; }
        this.element.removeClass( "ui-state-active" );
      });
      
    this.ranges.each( function( i ) {
			$( this ).data( "index.ui-slider-range", i );
		});
    // <-- OTTO

		this.handles
			.keydown(function( event ) {
				var ret = true,
					index = $( this ).data( "index.ui-slider-handle" ),
					allowed,
					curVal,
					newVal,
					step;
	
				if ( self.options.disabled ) {
					return;
				}
	
				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
					case $.ui.keyCode.END:
					case $.ui.keyCode.PAGE_UP:
					case $.ui.keyCode.PAGE_DOWN:
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						ret = false;
						if ( !self._keySliding ) {
							self._keySliding = true;
							$( this ).addClass( "ui-state-active" );
							allowed = self._start( event, index );
							if ( allowed === false ) {
								return;
							}
						}
						break;
				}
	
				step = self.options.step;
				if ( self.options.values && self.options.values.length ) {
					curVal = newVal = self.values( index );
				} else {
					curVal = newVal = self.value();
				}
	
				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
						newVal = self._valueMin();
						break;
					case $.ui.keyCode.END:
						newVal = self._valueMax();
						break;
					case $.ui.keyCode.PAGE_UP:
						newVal = self._trimAlignValue( curVal + ( (self._valueMax() - self._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.PAGE_DOWN:
						newVal = self._trimAlignValue( curVal - ( (self._valueMax() - self._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
						if ( curVal === self._valueMax() ) {
							return;
						}
						newVal = self._trimAlignValue( curVal + step );
						break;
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						if ( curVal === self._valueMin() ) {
							return;
						}
						newVal = self._trimAlignValue( curVal - step );
						break;
				}
	
				self._slide( event, index, newVal );
	
				return ret;
	
			})
			.keyup(function( event ) {
				var index = $( this ).data( "index.ui-slider-handle" );
	
				if ( self._keySliding ) {
					self._keySliding = false;
					self._stop( event, index );
					self._change( event, index );
					$( this ).removeClass( "ui-state-active" );
				}
	
			});

		this._refreshValue();
    this._changeDependency();

		this._animateOff = false;
	},

	destroy: function() {
		this.handles.remove();
		this.range.remove();

		this.element
			.removeClass( "ui-slider" +
				" ui-slider-horizontal" +
				" ui-slider-vertical" +
				" ui-slider-disabled" +
				" ui-widget" +
				" ui-widget-content" +
				" ui-corner-all" )
			.removeData( "slider" )
			.unbind( ".slider" );

		this._mouseDestroy();

		return this;
	},

	_mouseCapture: function( event ) {
		var o = this.options,
			position,
			normValue,
			distance,
			closestHandle,
			self,
			index,
			allowed,
			offset,
			mouseOverHandle;

		if ( o.disabled ) {
			return false;
		}

		this.elementSize = {
			width: this.element.outerWidth(),
			height: this.element.outerHeight()
		};
		this.elementOffset = this.element.offset();

		position = { x: event.pageX, y: event.pageY };
		normValue = this._normValueFromMouse( position );
		distance = this._valueMax() - this._valueMin() + 1;
		self = this;
		this.handles.each(function( i ) {
			var thisDistance = Math.abs( normValue - self.values(i) );
			if ( distance > thisDistance ) {
				distance = thisDistance;
				closestHandle = $( this );
				index = i;
			}
		});

		// workaround for bug #3736 (if both handles of a range are at 0,
		// the first is always used as the one with least distance,
		// and moving it is obviously prevented by preventing negative ranges)
		if( o.range === true && this.values(1) === o.min ) {
			index += 1;
			closestHandle = $( this.handles[index] );
		}

		allowed = this._start( event, index );
		if ( allowed === false ) {
			return false;
		}
		this._mouseSliding = true;

		self._handleIndex = index;
    
    /* OTTO -->
		closestHandle
			.addClass( "ui-state-active" )
			.focus();
    <-- OTTO */
		
		offset = closestHandle.offset();
		mouseOverHandle = !$( event.target ).parents().andSelf().is( ".ui-slider-handle" );
		this._clickOffset = mouseOverHandle ? { left: 0, top: 0 } : {
			left: event.pageX - offset.left - ( closestHandle.width() / 2 ),
			top: event.pageY - offset.top -
				( closestHandle.height() / 2 ) -
				( parseInt( closestHandle.css("borderTopWidth"), 10 ) || 0 ) -
				( parseInt( closestHandle.css("borderBottomWidth"), 10 ) || 0) +
				( parseInt( closestHandle.css("marginTop"), 10 ) || 0)
		};
    
    // OTTO -->
    if( self.options.dblchange ){
      this.element.unbind( "dblclick" );
      this.element.bind( "dblclick", function(){
        if( self.ranges.hasClass( "ui-state-hover" ) ){
          self.ranges.each(function( i ){
            if( $( this ).hasClass( "ui-state-hover" ) ) {
              self._deleteGroup( i );
              self._isChange();
            }
          })
        }else{
          self._createGroup( normValue );
          self._isChange();
        }
      });
    }
    
    if( this.ranges.hasClass( "ui-state-hover" ) && o.slideGroup === true ){
      o.mousePuffer = normValue;
      //this._slideGroup( event, index, normValue );
    }else{
  		closestHandle
  			.addClass( "ui-state-active" )
  			.focus();
			//this._slide( event, index, normValue );
		} 
    // <-- OTTO

    /* OTTO -->
		if ( !this.handles.hasClass( "ui-state-hover" ) ) {
			this._slide( event, index, normValue );
		} 
    --> OTTO */
    
		this._animateOff = true;
		return true;
	},

	_mouseStart: function( event ) {
		return true;
	},

	_mouseDrag: function( event ) {
		var position = { x: event.pageX, y: event.pageY },
			normValue = this._normValueFromMouse( position );
      
    // OTTO -->
    if( this.handles.eq( this._handleIndex ).hasClass( "ui-state-active" ) === false && this.options.slideGroup === true ){
      this._slideGroup( event, this._handleIndex, normValue );
    }else{
      this._slide( event, this._handleIndex, normValue );
    }
    // <-- OTTO
		
    /* OTTO -->
		this._slide( event, this._handleIndex, normValue );
    <-- OTTO */

		return false;
	},

	_mouseStop: function( event ) {
		this.handles.removeClass( "ui-state-active" );
		this._mouseSliding = false;

		this._stop( event, this._handleIndex );
		this._change( event, this._handleIndex );

		this._handleIndex = null;
		this._clickOffset = null;
		this._animateOff = false;

		return false;
	},
	
	_detectOrientation: function() {
		this.orientation = ( this.options.orientation === "vertical" ) ? "vertical" : "horizontal";
	},

	_normValueFromMouse: function( position ) {
		var pixelTotal,
			pixelMouse,
			percentMouse,
			valueTotal,
			valueMouse;

		if ( this.orientation === "horizontal" ) {
			pixelTotal = this.elementSize.width;
			pixelMouse = position.x - this.elementOffset.left - ( this._clickOffset ? this._clickOffset.left : 0 );
		} else {
			pixelTotal = this.elementSize.height;
			pixelMouse = position.y - this.elementOffset.top - ( this._clickOffset ? this._clickOffset.top : 0 );
		}

		percentMouse = ( pixelMouse / pixelTotal );
		if ( percentMouse > 1 ) {
			percentMouse = 1;
		}
		if ( percentMouse < 0 ) {
			percentMouse = 0;
		}
		if ( this.orientation === "vertical" ) {
			percentMouse = 1 - percentMouse;
		}

		valueTotal = this._valueMax() - this._valueMin();
		valueMouse = this._valueMin() + percentMouse * valueTotal;

		return this._trimAlignValue( valueMouse );
	},

	_start: function( event, index ) {
		var uiHash = {
			handle: this.handles[ index ],
			value: this.value()
		};
		if ( this.options.values && this.options.values.length ) {
			uiHash.value = this.values( index );
			uiHash.values = this.values();
		}
		return this._trigger( "start", event, uiHash );
	},

	_slide: function( event, index, newVal ) {
		var otherVal,
			newValues,
			allowed;

		if ( this.options.values && this.options.values.length ) {
    
      // OTTO -->
      if( this._pushSlider( index, newVal ) === false ){
        newVal = this.values( index );
      }
      // <-- OTTO
    
			otherVal = this.values( index ? 0 : 1 );

			if ( ( this.options.values.length === 2 && this.options.range === true ) && 
					( ( index === 0 && newVal > otherVal) || ( index === 1 && newVal < otherVal ) )
				) {
				newVal = otherVal;
			}

			if ( newVal !== this.values( index ) ) {
				newValues = this.values();
				newValues[ index ] = newVal;
				// A slide can be canceled by returning false from the slide callback
				allowed = this._trigger( "slide", event, {
					handle: this.handles[ index ],
					value: newVal,
					values: newValues
				} );
				otherVal = this.values( index ? 0 : 1 );
				if ( allowed !== false ) {
					this.values( index, newVal, true );
				}
			}
		} else {
			if ( newVal !== this.value() ) {
				// A slide can be canceled by returning false from the slide callback
				allowed = this._trigger( "slide", event, {
					handle: this.handles[ index ],
					value: newVal
				} );
				if ( allowed !== false ) {
					this.value( newVal );
				}
			}
		}
    this._changeIDvalues();
    this._isChange();
	},

	_stop: function( event, index ) {
		var uiHash = {
			handle: this.handles[ index ],
			value: this.value()
		};
		if ( this.options.values && this.options.values.length ) {
			uiHash.value = this.values( index );
			uiHash.values = this.values();
		}

		this._trigger( "stop", event, uiHash );
	},

	_change: function( event, index ) {
		if ( !this._keySliding && !this._mouseSliding ) {
			var uiHash = {
				handle: this.handles[ index ],
				value: this.value()
			};
			if ( this.options.values && this.options.values.length ) {
				uiHash.value = this.values( index );
				uiHash.values = this.values();
			}

			this._trigger( "change", event, uiHash );
		}
	},

	value: function( newValue ) {
		if ( arguments.length ) {
			this.options.value = this._trimAlignValue( newValue );
			this._refreshValue();
			this._change( null, 0 );
			return;
		}

		return this._value();
	},

	values: function( index, newValue ) {
		var vals,
			newValues,
			i;

		if ( arguments.length > 1 ) {
			this.options.values[ index ] = this._trimAlignValue( newValue );
			this._refreshValue();
			this._change( null, index );
			return;
		}

		if ( arguments.length ) {
			if ( $.isArray( arguments[ 0 ] ) ) {
				vals = this.options.values;
				newValues = arguments[ 0 ];
				for ( i = 0; i < vals.length; i += 1 ) {
					vals[ i ] = this._trimAlignValue( newValues[ i ] );
					this._change( null, i );
				}
				this._refreshValue();
			} else {
				if ( this.options.values && this.options.values.length ) {
					return this._values( index );
				} else {
					return this.value();
				}
			}
		} else {
			return this._values();
		}
	},

	_setOption: function( key, value ) {
		var i,
			valsLength = 0;

		if ( $.isArray( this.options.values ) ) {
			valsLength = this.options.values.length;
		}

		$.Widget.prototype._setOption.apply( this, arguments );

		switch ( key ) {
			case "disabled":
				if ( value ) {
					this.handles.filter( ".ui-state-focus" ).blur();
					this.handles.removeClass( "ui-state-hover" );
					this.handles.attr( "disabled", "disabled" );
					this.element.addClass( "ui-disabled" );
				} else {
					this.handles.removeAttr( "disabled" );
					this.element.removeClass( "ui-disabled" );
				}
				break;
			case "orientation":
				this._detectOrientation();
				this.element
					.removeClass( "ui-slider-horizontal ui-slider-vertical" )
					.addClass( "ui-slider-" + this.orientation );
				this._refreshValue();
				break;
			case "value":
				this._animateOff = true;
				this._refreshValue();
				this._change( null, 0 );
				this._animateOff = false;
				break;
			case "values":
				this._animateOff = true;
				this._refreshValue();
				for ( i = 0; i < valsLength; i += 1 ) {
					this._change( null, i );
				}
				this._animateOff = false;
				break;
		}
	},

	//internal value getter
	// _value() returns value trimmed by min and max, aligned by step
	_value: function() {
		var val = this.options.value;
		val = this._trimAlignValue( val );

		return val;
	},

	//internal values getter
	// _values() returns array of values trimmed by min and max, aligned by step
	// _values( index ) returns single value trimmed by min and max, aligned by step
	_values: function( index ) {
		var val,
			vals,
			i;

		if ( arguments.length ) {
			val = this.options.values[ index ];
			val = this._trimAlignValue( val );

			return val;
		} else {
			// .slice() creates a copy of the array
			// this copy gets trimmed by min and max and then returned
			vals = this.options.values.slice();
			for ( i = 0; i < vals.length; i+= 1) {
				vals[ i ] = this._trimAlignValue( vals[ i ] );
			}

			return vals;
		}
	},
	
	// returns the step-aligned value that val is closest to, between (inclusive) min and max
	_trimAlignValue: function( val ) {
		if ( val <= this._valueMin() ) {
			return this._valueMin();
		}
		if ( val >= this._valueMax() ) {
			return this._valueMax();
		}
		var step = ( this.options.step > 0 ) ? this.options.step : 1,
			valModStep = (val - this._valueMin()) % step;
			alignValue = val - valModStep;

		if ( Math.abs(valModStep) * 2 >= step ) {
			alignValue += ( valModStep > 0 ) ? step : ( -step );
		}

		// Since JavaScript has problems with large floats, round
		// the final value to 5 digits after the decimal point (see #4124)
		return parseFloat( alignValue.toFixed(5) );
	},

	_valueMin: function() {
		return this.options.min;
	},

	_valueMax: function() {
		return this.options.max;
	},
	
	_refreshValue: function() {
		var oRange = this.options.range,
			o = this.options,
			self = this,
			animate = ( !this._animateOff ) ? o.animate : false,
			valPercent,
			_set = {},
			lastValPercent,
			value,
			valueMin,
			valueMax;

		if ( this.options.values && this.options.values.length ) {
			this.handles.each(function( i, j ) {
				valPercent = ( self.values(i) - self._valueMin() ) / ( self._valueMax() - self._valueMin() ) * 100;
				_set[ self.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
				$( this ).stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );
        if( !( o.values.length % 2 ) && o.ranges ){
        // OTTO -->
          var valswitch = ( i % 2 ),
            rangesswitch = Math.floor( i / 2 );
    			if ( self.orientation === "horizontal" ) {
    				if ( valswitch === 0 ) {
    					self.ranges.eq( rangesswitch ).stop( 1, 1 )[ animate ? "animate" : "css" ]( { left: valPercent + "%" }, o.animate );
    				}
    				if ( valswitch === 1 ) {
    					self.ranges.eq( rangesswitch )[ animate ? "animate" : "css" ]( { width: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
    				}
    			} else {
    				if ( valswitch === 0 ) {
    					self.ranges.eq( rangesswitch ).stop( 1, 1 )[ animate ? "animate" : "css" ]( { bottom: ( valPercent ) + "%" }, o.animate );
    				}
    				if ( valswitch === 1 ) {
    					self.ranges.eq( rangesswitch )[ animate ? "animate" : "css" ]( { height: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
    				}
          }
        // <-- OTTO
        }else{
  				if ( self.options.range === true ) {
  					if ( self.orientation === "horizontal" ) {
  						if ( i === 0 ) {
  							self.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { left: valPercent + "%" }, o.animate );
  						}
  						if ( i === 1 ) {
  							self.range[ animate ? "animate" : "css" ]( { width: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
  						}
  					} else {
  						if ( i === 0 ) {
  							self.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { bottom: ( valPercent ) + "%" }, o.animate );
  						}
  						if ( i === 1 ) {
  							self.range[ animate ? "animate" : "css" ]( { height: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
  						}
  					}
  				}
        }
				lastValPercent = valPercent;
			});
		} else {
			value = this.value();
			valueMin = this._valueMin();
			valueMax = this._valueMax();
			valPercent = ( valueMax !== valueMin ) ?
					( value - valueMin ) / ( valueMax - valueMin ) * 100 :
					0;
			_set[ self.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
			this.handle.stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );

			if ( oRange === "min" && this.orientation === "horizontal" ) {
				this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { width: valPercent + "%" }, o.animate );
			}
			if ( oRange === "max" && this.orientation === "horizontal" ) {
				this.range[ animate ? "animate" : "css" ]( { width: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
			}
			if ( oRange === "min" && this.orientation === "vertical" ) {
				this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { height: valPercent + "%" }, o.animate );
			}
			if ( oRange === "max" && this.orientation === "vertical" ) {
				this.range[ animate ? "animate" : "css" ]( { height: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
			}
		}
	},
  
  /*******************************************************************************************************************************************************************************/
  /******************************* --> OTTO <-- ******************************* 27 SEPTEMBER 2011 ********************************** --> OTTO <-- ********************************/
  /*******************************************************************************************************************************************************************************/
  
  _pushSlider: function( index, newVal ){
    var self = this,
      o = this.options,
      curVal = self.values( index ),
      slide = true,
      thisVal;
      
    this.handles.each(function( i ){
      thisVal = self.values( i );
      if( i != index && ( thisVal >= newVal && thisVal < curVal ) || ( thisVal <= newVal && thisVal > curVal ) ){
      
        if( curVal > newVal ){
          newVal -= o.step;
        }else{
          newVal += o.step;
        }
        
        if( !( newVal > self._valueMax() ) && !( newVal < self._valueMin() ) && o.pushSlider === true ){
          if( self._pushSlider( i, newVal ) === true ){
            self.values( i , newVal , true );
          } else {
            slide = false;
          }
        } else {
          slide = false;
        }
        
      }
    });
    if( slide )
    return true;
    return false;
  },
  
  _slideGroup: function( event, index, normValue ){
    
    var o = this.options,
      slider1 = ( index % 2 ? index - 1 : index ),
      slider2 = ( index % 2 ? index : index + 1 ),    
      valSlider1 = this.values( slider1 ),
      valSlider2 = this.values( slider2 ),
      mouseDiff = normValue - o.mousePuffer;
    
    /*
    // WENN MAN BIS AUF 0 ZIEHEN MÖCHTE FREISCHALTEN
    var sliderDiff = ( valSlider2 - valSlider1 );
    if( sliderDiff % 2 ) {
      sliderDiff += this.options.step;
      if( ( sliderDiff / 2 ) + valSlider1 > normValue && valSlider1 != 0 && valSlider2 !=){
        valSlider1 += this.options.step;
      } else {
        valSlider2 -= this.options.step;
      }
    }
    */
    
    valSlider1 += mouseDiff;
    valSlider2 += mouseDiff;
    
		this._slide( event, slider1, valSlider1 );
    this._slide( event, slider2, valSlider2 );
    o.mousePuffer = normValue;
  },
  
  _deleteGroup: function( rangeIndex ){
    var self = this,
      o = this.options,
      thisRange = $( this.ranges[ rangeIndex ] );
      
    $( self.handles ).each(function( i ){
      if( $( this ).css( "left" ) == thisRange.css( "left" ) ){
        o.values.splice( i, 2 );
        $( this ).remove();
        $( self.handles[self._handlePartner(i)] ).remove();
        self.handles.splice( i, 2 );
      }
    });
    thisRange.remove();
    self.ranges.splice( rangeIndex, 1 );
    
    self._changeDependency();
  },
  
  _createGroup: function( normValue ){
    if( this.ranges.length < this.options.ids.length ){
    
      var o = this.options,
          stepDistance = ( 5 * o.step );
      
      if( normValue === undefined ){
        var maxVal = 0,
          count = 0,
          indexHandle,
          indexHandleDefault;
        
        // ERSTER VERSUCH ( BESTEHENDES UEBERNEHMEN AUS PUFFER )
        for( var i = 0; i < o.values.length; i++ ){
          if( o.values[i] > maxVal ){
            maxVal = o.values[i];
            indexHandleDefault = i;
          }
        }
        for( var i = 0; i < o.puffer.length; i++ ){
          if( o.puffer[ i ] > maxVal){
            if( !(i % 2) && indexHandle === undefined ){
              indexHandle = i;
            }
          }
        }
        if( indexHandle !== undefined ){
          o.values.push( o.puffer[indexHandle] );
          o.values.push( o.puffer[indexHandle + 1] );
          this._create();
          return true;
        }
        
        // ZWEITER VERSUCH ( AM ENDE EINEN NEUEN EINFÜGEN )
        var distance = ( this._valueMax() - o.values[ indexHandleDefault ] ),
          newValHandle1 = ( this._valueMax() - stepDistance ),
          newValHandle2 = this._valueMax(),
          middle;
          
        if( distance <= ( stepDistance + 1 ) ){
          // 2.0
          this._slide( null, indexHandleDefault, ( newValHandle1 - 1 ) );
        }else{
          // 2.1
          middle = parseInt( distance / 2 );
          newValHandle1 = this._valueMax() - middle - parseInt( middle / 2 );
          newValHandle2 = this._valueMax() - middle + parseInt( middle / 2 );
        }
        
        o.values.push( newValHandle1, newValHandle2 );
        this._create();
        return true;      
      
      }else{
        
        // Doppelklick
        for( var i = 0; i < this.handles.length; i += 2 ){
          if( o.values[i] < normValue && normValue < o.values[i+1] ){
            return false;
          }
        }
        
        var newValHandle1 = normValue - 2,
          newValHandle2 = newValHandle1 + stepDistance,
          self = this;
          
        for( var i = 0; i < this.handles.length; i += 2 ){
          if( o.values[i] < newValHandle1 && newValHandle1 <= o.values[i+1] ){
            this._slide( null, i+1, newValHandle1 - o.step );
          }
          if( o.values[i] <= newValHandle2 && newValHandle2 < o.values[i+1] ){
            this._slide( null, i, newValHandle2 + o.step );
          }
        }
        
        o.values.push( newValHandle1, newValHandle2 );
        this._create();
        return true; 
      }
      
    }
  },
  
  _isChange: function(){
    this._trigger( "isChange" );
  },
  
  resetSlider: function( infos ){
    this.addGroup( 0 );
    this.options.values = infos;
    this._create();
  },
  
  addGroup: function( count ){
    var o = this.options,
      indexRange,
      valRange,
      countRanges;
      
    if( count > o.ids.length ) count = o.ids.length;
      
    countRanges = this.ranges.length;
    
    while( countRanges != count ){
      valRange = 0;
      if( this.ranges.length < count ){
        this._createGroup();
        countRanges++;
      }else{
        this.ranges.each(function( i ){
          if( parseFloat( $(this).css( "left" ) ) > valRange ){
            valRange = i;
          }
        });
        this._deleteGroup( valRange );
        countRanges--;
      }
    }
  },
  
  _changeDependency: function() {
    var o = this.options;
    if( o.dependency.length > 0 ){
      $( o.dependency ).slider( "value", this.ranges.length );
    }
    this._changeIDvalues();
  },
  
  _changeIDvalues: function() {
    var o = this.options,
      self = this;
    for( var i = 0; i < o.ids.length; i++ ){
      $( "#" + o.ids[ i ] ).val( function() {
        if( ( ( i * 2 ) + 1 ) >= o.values.length || ( i * 2 ) > o.values.length ){
          return o.defaultValue+";"+o.defaultValue;
        }else{
          return ( self.values( i * 2 ) + ";" + self.values( i * 2 + 1 ) );
        }
      });
    }
  },
  
  _handlePartner: function( i ){
    return ( i%2 ? i-1 : i+1 );
  },
  
  _checkForDoubleTap: function( thisE ){
    var o = this.options;
    
    if( ( new Date() - thisE.dbltouchtime ) < o.touchdblclicktime ){
      thisE.dbltouchtime = 0;
      return true;
    }else{
      thisE.dbltouchtime = new Date();
      return false;
    }
  }
  // <-- OTTO
});

$.extend( $.ui.slider, {
	version: "1.8.14"
});

}(jQuery));
