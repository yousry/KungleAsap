/**
 * jQuery DrawBox Plug-In 0.6
 *
 * http://github.com/crowdsavings/drawbox
 * http://plugins.jquery.com/project/drawbox
 *
 * Author: Josh Sherman <josh@crowdsavings.com>
 * Copyright (c) 2010 CrowdSavings.com, LLC
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 * (Modified for dialogs: Yousry Abdallah)
 * (Modified for simple scribble: Yousry Abdallah) 
 */



(function($)
{
	$.fn.extend(
	{
		drawbox: function(options)
		{
			var defaults = {
				lineWidth:     1.0,
				lineCap:       'butt',
				lineJoin:      'miter',
				miterLimit:    10,
				strokeStyle:   'blue',
				fillStyle:     'transparent',
				shadowOffsetX: 0.0,
				shadowOffsetY: 0.0,
				shadowBlur:    0.0,
				shadowColor:   'transparent black', 
				// Color selector options
				colorSelector: false,
				colors:        [ 'blue', 'black' ],
				// Clearing options
				showClear:     true,
				clearLabel:    'Clear Note',
				clearStyle:    'button' // or 'link'
			}

			var options = $.extend(defaults, options);

			return this.each(function()
			{
				if (this.nodeName == 'canvas')
				{
					$(this).css('cursor',   'pointer');
					$(this).attr('onclick', 'function onclick(event) { void 1; }');

					if (this.getContext)
					{
						var canvas  = this;
						var context = this.getContext('2d');
						var id = $(this).attr('id');

						$(this).after('<div id="' + id + '-controls" style="width:' + $(this).width() + 'px"></div>');

						context.underInteractionEnabled = true;

						// Overrides with passed options
						
						context.lineWidth     = options.lineWidth;
						context.lineCap       = options.lineCap;
						context.lineJoin      = options.lineJoin;
						context.miterLimit    = options.miterLimit;
						context.strokeStyle   = options.strokeStyle;
						context.fillStyle     = options.fillStyle;
						context.shadowOffsetX = options.shadowOffsetX;
						context.shadowOffsetY = options.shadowOffsetY;
						context.shadowBlur    = options.shadowBlur;
						context.shadowColor   = options.shadowColor;
						
						// Adds the color selector
						if (options.colorSelector == true)
						{
							var color_selector = '';

							for (i in options.colors)
							{
								var color = options.colors[i];

								if (i == 0)
								{
									context.strokeStyle = color
								}

								color_selector = color_selector + '<div style="height:16px;width:16px;background-color:' + color + ';margin:2px;float:left;border:2px solid ' + (i == 0 ? '#fff' : 'transparent') + '"' + (i == 0 ? ' class="selected"' : '') + '></div>';
							}
							$('#' + id + '-controls').append('<div style="float:left" id="' + id + '-colors">' + color_selector + '</div>');
						}
					
						// Adds the clear button / link
						if (options.showClear == true)
						{
							var clear_tag = (options.clearStyle == 'link' ? 'div' : 'button');
							$('#' + id + '-controls').append('<' + clear_tag + ' id="' + id + '-clear" style="float:right">' + options.clearLabel + '</' + clear_tag + '><br style="clear:both" />');
							clear = true;
						}

						var data_input = id + '-data';
						$(this).after('<input type="hidden" id="' + data_input + '" name="' + data_input + '" />');

                        var thisIsMe = $(this)
						var drawing = false;
						var prevX   = false;
						var prevY   = false;
						var x       = false;
						var y       = false;


						// Mouse events
						$(this).mousedown(function(e) { drawingStart(e); });
						$(this).mousemove(function(e) { drawingMove(e);  });
						$(this).mouseup(  function()  { drawingStop();   });

						// Touch events
						$(this).bind('touchstart',  function(e) { drawingStart(e); });
						$(this).bind('touchmove',   function(e) { drawingMove(e);  });
						$(this).bind('touchend',    function(e) { drawingStop(e);  });
						$(this).bind('touchcancel', function()  { drawingStop();   });
							
						// Other events
						$('#' + id + '-colors div').click(function(e)
						{
							$('#' + id + '-controls div').css('borderColor', 'transparent').removeClass('selected');
							$(this).addClass('selected');
							$(this).css('borderColor', '#fff');
						});

						$('#' + id + '-clear').click(function(e)
						{
                            addStroke("addStroke", "clear 0 0");
							context.save();
							context.beginPath();
							context.closePath();
							context.restore();
							context.clearRect(0, 0, $(canvas).width(), $(canvas).height());

							$('#' + data_input).val('');
						});

						function getTouch(e)
						{
							// iPhone/iPad/iPod uses event.touches and not the passed event
							if (typeof(event) != "undefined" && typeof(event.touches) != "undefined")
							{
								e = event.touches.item(0);
							}
							
							// Tracks last position to handle dots (as opposed to lines)
							if (x != false)
							{
								prevX = x;
								prevY = y;
							}

							// Calculates the X and Y values
							x = e.clientX - thisIsMe.offset().left;
							y = e.clientY - thisIsMe.offset().top;
							return e;
						}

						function draw(type)
						{
							var element = $('#' + data_input);
							var svg_data = element.val();
							
							if (type == 'start')
							{
							
								// Adds the SVG header tags
								if (svg_data == '')
								{
									svg_data = '<?xml version="1.0" standalone="no"?><!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"><svg width="' + $('#' + id).width() + '" height="' + $('#' + id).height() + '" version="1.1" xmlns="http://www.w3.org/2000/svg">';
								}
								else
								{
									svg_data = svg_data.substring(0, svg_data.length - 6);
								}
							}

							if (type != 'stop')
							{
								if (svg_data.length > 0)
								{
									svg_data = svg_data + ' ';
								}

								if (type == 'start')
								{
									prevX = false;
									prevY = false;

									context.beginPath();
									context.moveTo(x, y);

									svg_data = svg_data + '<polyline points="';
								}
								else if (type == 'move')
								{
									// If there's no previous increment since it's a .
									if (prevX == false)
									{
										x = x + 1;
										y = y + 1;
									}

									context.lineTo(x, y);
								}

								context.stroke();
								svg_data = svg_data + x + ',' + y + ' ';
							}
							else
							{
								draw('move');

								// Closes the polyline (with style info) and adds the closing svg tag
								svg_data = svg_data + '" style="fill:' + context.fillStyle + ';stroke:' + context.strokeStyle + ';stroke-width:' + context.lineWidth + '" /></svg>';
							}
                                                    
                            addStroke("addStroke", type + " " + Math.ceil(x) + " " + Math.ceil(y) );
							element.val(svg_data);
						}

						function drawingStart(e)
						{
							drawing = true;
							e = getTouch(e);
							context.strokeStyle = $('#' + id + '-colors div.selected').css('backgroundColor');
							draw('start');
						}
							
						function drawingMove(e)
						{
							// Keeps iPad from scrolling while drawing
							e.preventDefault();
						
							if (drawing == true)
							{
								e = getTouch(e);

								draw('move');
							}
						}

						function drawingStop()
						{
							drawing = false;

							// Draws one last line so we can draw dots (e.g. i)
							draw('stop');
						}
					}
					else
					{
						alert('Your browser does not support <canvas>');
					}
				}
				else
				{
					alert('.drawbox() only works on <canvas> elements');					
				}
			});
		}
	});
})(jQuery);
