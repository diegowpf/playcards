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

var fieldWidth = 1443;
var fieldHeight = 767;
var numSections = 3;
var borderWidth = 100
var usableFieldWidth = fieldWidth-borderWidth
var ratio = 1343/255.6
var standardFieldWidthInFt = 160
var standardDownHeightInFt = 30
var defaultGoSteps = 15

var playerWidth = (usableFieldWidth) / (fieldWidth/32);
var gridSize = usableFieldWidth / standardFieldWidthInFt
var yard = gridSize * 3

var line = 510;
var routeColor = "navy";

// var players = [
  // { placement: { relativeX: 80, relativeY: 0 }, pos: "center" },
  // { placement: { relativeX: 72, relativeY: 0 }, pos: "lt" },
  // { placement: { relativeX: 64, relativeY: 0 }, pos: "lt" },
  // { placement: { relativeX: 88, relativeY: 0 }, pos: "rt" },
  // { placement: { relativeX: 96, relativeY: 0 }, pos: "rg" },
  // { placement: { relativeX: 102, relativeY: 6 }, pos: "te", tag: 'H' },
  // { placement: { relativeX: 20, relativeY: 10 }, pos: "wr", tag: "W" ,
  //   route:[{move: "go"}]},
  // { placement: { relativeX: 30, relativeY: 10 }, pos: "wr", tag: "Y" ,
  //   route:[{steps: 5, move: "curl"}]},
  // { placement: { relativeX: 143, relativeY: 10 }, pos: "wr", tag: "Z",
  //   route:[{steps: 5, move: "curl"}] },
  // { placement: { relativeX: 80, relativeY: 9 }, pos: "qb" },
  // { placement: { relativeX: 80, relativeY: 18 }, pos: "rb", tag: "F",
  //   motion:[{steps: 4, direction: "left"}]}
//   { placement: { relativeX: 91, relativeY: 0}, pos: "wr", tag: "19" },
// { placement: { relativeX: 67, relativeY: 0}, pos: "wr", tag: "19" },
// { placement: { relativeX: 83, relativeY: 0}, pos: "wr", tag: "19" },
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
        axios.get("http://server.immersivesports.ai/playcards/123e4567-e89b-12d3-a456-426655440000")
          .then(res => {
            const placements = res.data.playerMarkers;
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
        .attr("style", "font: italic 40px serif; fill: red")
      label.text(player.tag)

      if( player.route) {
        player.route.forEach((x) => {
          switch( x.move ){
            case "curl":
              this.generateComebackRoute(g, player, coordinates, x)
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

    generateComebackRoute(g, player, coordinates, route ){
      var inverter = 1;

      if( player.placement.relativeX > 80 ){
        inverter = -1;
      }

      var path = "M " + (coordinates.x + playerWidth) + " " + coordinates.y + " " +
        (coordinates.x + playerWidth) + " " + (coordinates.y - ((route.steps) * yard)) + " " +
        (coordinates.x + playerWidth + (inverter * ((2 * yard)))) + " " + (coordinates.y - ((route.steps) * yard)) + " " +
        (coordinates.x + playerWidth + (inverter * ((2 * yard)))) + " " + (coordinates.y - (route.steps * yard) + yard)

        this.generatePlayerRoute(g, path);
    }

    generatePlayerRoute(g, path ){
      g.append("path")
        .attr("marker-end", "url(#head)")
        .attr("d", path)
        .attr("fill", "none" )
        .attr("stroke",routeColor)
    }

    addNonSkillPlayer(svg, player){
      var coordinates = this.convertX(player.placement.relativeX, player.placement.relativeY)
      var icon = svg.append( "circle" );

      icon.attr( "cx", coordinates.x + playerWidth )
        .attr( "cy", coordinates.y + (playerWidth) )
        .attr( "r", playerWidth )
        .attr( "fill", "red" )
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
                  <svg viewBox="0 0 1443 767" preserveAspectRatio="xMidYMid meet" width="100%" height={fieldHeight} style={outerField} id="svg">

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
