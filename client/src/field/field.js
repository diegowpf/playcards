import React from 'react';
import * as d3 from "d3";
import axios from 'axios'

var props = {stroke: "white", "stroke-width": 5}
var outerField = {
  // "stroke-width":5,
  // "fill-opacity":0.1,
  // "-webkit-transform": "rotateX(-45deg)",
  // "-webkit-transform-origin": "bottom center",
  //  "-webkit-transform": "perspective(700px) rotateX(45deg)",
  // "-webkit-transform-style": "preserve-3d",
  // "position": "absolute",
  "top": "0px",
  "left": "0px"
}

var fieldStyle={
  "-webkit-transform": "rotate(90deg)",
  "-moz-transform": "rotate(90deg)",
  "-o-transform": "rotate(90deg)",
  "-ms-transform": "rotate(90deg)",
  "transform": "rotate(90deg)"
}

var fontSize = 30;
var fieldWidth = 1443;
var fieldHeight = 767;
var numSections = 3;
var borderWidth = 100
var usableFieldWidth = fieldWidth-borderWidth
var ratio = 1343/255.6
var standardFieldWidthInFt = 160
var standardDownHeightInFt = 30
var defaultGoSteps = 15

var playerWidth = (usableFieldWidth) / (fieldWidth/28);
var gridSize = usableFieldWidth / standardFieldWidthInFt
var yard = gridSize * 3

var line = 510;
var routeColor = "navy";

 // var players = [ [{"placement":{"relativeX":72,"relativeY":0},"pos":"lt","center":false},{"placement":{"relativeX":64,"relativeY":0},"pos":"lg","center":false},{"placement":{"relativeX":88,"relativeY":0},"pos":"rt","center":false},{"placement":{"relativeX":96,"relativeY":0},"pos":"rg","center":false},{"placement":{"relativeX":80,"relativeY":0},"pos":"C","center":true},{"placement":{"relativeX":80,"relativeY":8},"pos":"QB","tag":"QB","center":false},{"placement":{"relativeX":20,"relativeY":2},"pos":"wr","tag":"X","center":false},{"placement":{"relativeX":145,"relativeY":8},"pos":"wr","tag":"Y","center":false},{"placement":{"relativeX":80,"relativeY":16},"pos":"FB","tag":"FB","center":false},{"placement":{"relativeX":80,"relativeY":24},"pos":"HB","tag":"HB","center":false},{"placement":{"relativeX":104,"relativeY":2},"pos":"te","tag":"T","center":false}];

var players = [
  { placement: { relativeX: 30, relativeY: 10 }, pos: "wr", tag: "Y" ,
    routes:[{distance: 5, move: "go"}]},
  { placement: { relativeX: 143, relativeY: 10 }, pos: "wr", tag: "Z",
    routes:[{distance: 5, move: "curl"}] },
  { placement: { relativeX: 80, relativeY: 9 }, pos: "qb" },
  { placement: { relativeX: 80, relativeY: 18 }, pos: "rb", tag: "F",
    routes:[{distance: 4, move: ""}]},
  { placement: { relativeX: 91, relativeY: 0}, pos: "wr", tag: "19" },
{ placement: { relativeX: 67, relativeY: 0}, pos: "wr", tag: "19" },
{ placement: { relativeX: 83, relativeY: 0}, pos: "wr", tag: "19" }]
// { placement: { relativeX: 59, relativeY: 0}, pos: "wr", tag: "19" },
  // { placement: { relativeX: 11, relativeY: 0}, pos: "wr", tag: "84" },
  // { placement: { relativeX: 70, relativeY: 15}, pos: "wr", tag: "7" },
  // { placement: { relativeX: 58, relativeY: 15}, pos: "wr", tag: "30" },
  // { placement: { relativeX: 95, relativeY: 7}, pos: "wr", tag: "89" },
  // { placement: { relativeX: 150, relativeY: 1}, pos: "wr", tag: "11" },
  // { placement: { relativeX: 109, relativeY: 3}, pos: "wr", tag: "19" }
//   { placement: { relativeX: 70, relativeY: 0}, pos: "center", tag: "" },
// { placement: { relativeX: 54, relativeY: 0}, pos: "wr", tag: "" },
// { placement: { relativeX: 78, relativeY: 0}, pos: "wr", tag: "" },
// { placement: { relativeX: 62, relativeY: 0}, pos: "wr", tag: "" },
// { placement: { relativeX: 86, relativeY: 0}, pos: "wr", tag: "" },
// { placement: { relativeX: 11, relativeY: 0}, pos: "wr", tag: "84" },
// { placement: { relativeX: 70, relativeY: 15}, pos: "wr", tag: "7" },
// { placement: { relativeX: 58, relativeY: 15}, pos: "wr", tag: "30" },
// { placement: { relativeX: 95, relativeY: 7}, pos: "wr", tag: "89" },
// { placement: { relativeX: 150, relativeY: 1}, pos: "wr", tag: "11" },
// { placement: { relativeX: 109, relativeY: 3}, pos: "wr", tag: "19" }
// ]

var serverUrl = process.env.REACT_APP_SERVER_URL;

class Field extends React.Component {

  state = {
    placements: []
  }
    // componentDidMount(){
    //   players.forEach( (x)=> {
    //       this.addPlayer(d3.select("#svg"), x)
    //   })
    //
    //
    // }

    componentDidMount() {
        axios.get("http://server.immersivesports.ai/playcards/team/c679919f-d524-3f75-ad2a-5161706e12a5")
          .then(res => {
            const placements = res.data[0].formation.playerMarkers;
            // const placements = players;
            console.log(JSON.stringify(placements));
            this.setState({ placements });
            placements.forEach( (x)=> {
                   this.addPlayer(d3.select("#svg"), x)
            })
          })
      }


    drawFieldGrid(x1,y1,x2,y2){
      var horizontalUnits = ((x2-x1)/10)
      var verticalUnits = ((y2-y1)/10)

      // generateGrid

    }

    addPlayer( svg, player ){
      if( player.center ){
        this.addCenter(svg, player)
      }else if(!player.tag){
        this.addNonSkillPlayer(svg, player)
      } else {
        this.addSkillPlayer(svg, player);
      }

    }

    convertX(xOrig, yOrig){
      return {x: (xOrig* gridSize) + (gridSize/2), y: (yOrig*gridSize) + line}
    }

    addSkillPlayer(svg, player){
      var coordinates = this.convertX(player.placement.relativeX, player.placement.relativeY)
      var g = svg.append("g")
      var icon = g.append( "circle" );

      icon.attr( "cx", coordinates.x + playerWidth )
        .attr( "cy", coordinates.y + (playerWidth) )
        .attr( "r", playerWidth )

      icon.attr( "stroke-width", "5px" )
      icon.attr( "stroke", "black")
      icon.attr("fill", "rgba(0,0,0,0)")


      var label = g.append( "text")
        .attr( "x", coordinates.x + playerWidth )
        .attr( "y", coordinates.y + playerWidth )
        .attr("text-anchor", "middle" )
        .attr("stroke", "black")
        .attr("stroke-width", "2px")
        .attr("dy", ".3em")
        .attr("style", "font: italic " + fontSize + "px serif; fill: red")
      label.text(player.tag)

      if( player.routes) {
        player.routes.forEach((x) => {
          switch( x.move ){
            case "curl":
              this.generateCurlRoute(g, player, coordinates, x)
              break;
            default:
              this.generateGoRoute(g, player, coordinates, x )
            }
          })
        }
    }

    generateGoRoute(g, player, coordinates, route ){
      var path = "M " + (coordinates.x + playerWidth) + " "
        + coordinates.y + " " + (coordinates.x + playerWidth) + " " +
        (coordinates.y - ((defaultGoSteps) * yard))

        this.generatePlayerRoute(g, path);
    }

    generateCurlRoute(g, player, coordinates, route ){
      var inverter = 1;

      if( player.placement.relativeX > 80 ){
        inverter = -1;
      }


      var path = "M " + (coordinates.x + playerWidth) + " " + coordinates.y + " " +
        (coordinates.x + playerWidth) + " " + (coordinates.y - ((route.distance) * yard)) + " " +
        (coordinates.x + playerWidth + (inverter * ((2 * yard)))) + " " + (coordinates.y - ((route.distance) * yard)) + " " +
        (coordinates.x + playerWidth + (inverter * ((2 * yard)))) + " " + (coordinates.y - (route.distance * yard) + yard)

        this.generatePlayerRoute(g, path);
    }

    generatePlayerRoute(g, path ){
      g.append("path")
        .attr("marker-end", "url(#head)")
        .attr("d", path)
        .attr("fill", "none" )
        .attr( "stroke-width", "5px" )
        .attr("stroke",routeColor)
    }

    addNonSkillPlayer(svg, player){
      var coordinates = this.convertX(player.placement.relativeX, player.placement.relativeY)
      var icon = svg.append( "circle" );

      icon.attr( "cx", coordinates.x + playerWidth )
        .attr( "cy", coordinates.y + (playerWidth) )
        .attr( "r", playerWidth )
        .attr( "fill", "red" )
        .attr( "stroke-width", "5px" )
        .attr( "stroke", "black")
    }

    addCenter(svg, player){
      var coordinates = this.convertX(player.placement.relativeX, player.placement.relativeY)
      svg.append( "rect" )
        .attr( "x", coordinates.x)
        .attr( "y", coordinates.y )
        .attr( "width", playerWidth * 2 )
        .attr( "height", playerWidth * 2)
        .attr( "fill", "black" )
    }

    render() {
        return (
            <div>
                  <svg viewBox="0 0 1443 767" preserveAspectRatio="xMidYMid meet" x="0"   y="0" width="100%" height={fieldHeight} style={outerField} id="svg">

                <defs>
                  <pattern id="img1" patternUnits="userSpaceOnUse" x="0" y="0" width={fieldWidth} height={fieldHeight}>
                      <image xlinkHref="images/ravens-30-no-logo.png" width={fieldWidth} height={fieldHeight}/>
                  </pattern>
              </defs>
              <path d="M5,5
                  l0,767 l1443,0 l0,-767 l-{fieldWidth},0"
                  fill="url(#img1)" />

                <marker id='head' orient='auto' markerWidth='2' markerHeight='4'
                        refX='0.1' refY='2'>
                      <path d='M0,0 V4 L2,2 Z' fill='red' />
                </marker>

                <path d="M200,200 M100,100" fill='black' />

                <polyline points="
                  1288.8961058267716 518.505 1282.3441587401576 296.4299007874016
                " fill="none" stroke="darkblue" stroke-width="5px"/>

                <polyline points="
                  1229.088 337.0650992125984 1279.656052913386 298.32
                " fill="none" stroke="darkblue" stroke-width="5px" />

                <polyline points="
                  355.99215874015755  112.94245039370071 177.57605291338587  237.52509921259843
                " fill="none" stroke="darkblue" stroke-width="5px" />

                <polyline points="
                  796.7732560629921 587.3882078740157 733.7732031496063 587.8608566929133
                " fill="none" stroke="darkblue" stroke-width="5px" />

                <polyline points="
                  796.6056264566931 562.1882078740157 736.1252031496065 561.4382598425198
                " fill="none" stroke="darkblue" stroke-width="5px" />


                <polyline points="
                  738.9812560629923 588.1758566929134 737.3011502362205 560.6132078740158
                " fill="none" stroke="darkblue" stroke-width="5px"/>

                <polyline points="
                  839.3280000000002 333.29432598425194 791.1118412598424 287.61922677165353
                " fill="none" stroke="darkblue" stroke-width="5px"/>


                <polyline points="
                947.1838941732283 542.2876538085937 981.1347675443526 520.8975076961517
981.1347675443526 520.8975076961517 997.4246443901892 508.78986905694006
997.4246443901892 508.78986905694006 1012.8923231588242 494.98715171813967
1012.8923231588242 494.98715171813967 1027.2639352122185 478.92432944655417
1027.2639352122185 478.92432944655417 1040.2656119123335 460.0363760089874
1040.2656119123335 460.0363760089874 1051.6234846211312 437.7582651722431
1051.6234846211312 437.7582651722431 1061.0636847005721 411.524970703125
1061.0636847005721 411.524970703125 1068.4009927678937 380.95981436043974
1068.4009927678937 380.95981436043974 1073.8201836629744 346.4394720196724
1073.8201836629744 346.4394720196724 1080.0002808919785 267.7933356285095
1080.0002808919785 267.7933356285095 1081.7961102987167 180.10677409887307
1081.7961102987167 180.10677409887307 1081.3998057943222 87.89999999999998
                " fill="none" stroke="darkblue" stroke-width="5px" />

                <polyline points="
                136.09079433070866 521.1824584656242 147.01703034793584 507.82936983399424
                147.01703034793584 507.82936983399424 157.23740243922916 491.65448410801923
                157.23740243922916 491.65448410801923 166.04680803119388 469.83599698357614
                166.04680803119388 469.83599698357614 172.74014455043522 439.55210415654216
                172.74014455043522 439.55210415654216 176.84550367938726 398.9216104929927
                176.84550367938726 398.9216104929927 178.83441305934636 349.8257106762412
                178.83441305934636 349.8257106762412 179.284815570943 237.52490078740158
                " fill="none" stroke="darkblue" stroke-width="5px" />


              </svg>

              <svg viewBox="0 0 1000 1000" >


              </svg>
            </div>
        );
    }
}
//
// PrimarySearchAppBar.propTypes = {
//     classes: PropTypes.object.isRequired,
// };

export default Field;
