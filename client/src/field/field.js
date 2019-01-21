import React from 'react';
import * as d3 from "d3";
import axios from 'axios'
import {connect} from 'react-redux';
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

// var players = [
//   { placement: { relativeX: 30, relativeY: 10 }, pos: "wr", tag: "Y" ,
//     routes:[{distance: 5, move: "go"}]},
//   { placement: { relativeX: 143, relativeY: 10 }, pos: "wr", tag: "Z",
//     routes:[{distance: 5, move: "curl"}] },
//   { placement: { relativeX: 80, relativeY: 9 }, pos: "qb" },
//   { placement: { relativeX: 80, relativeY: 18 }, pos: "rb", tag: "F",
//     routes:[{distance: 4, move: ""}]},
//   { placement: { relativeX: 91, relativeY: 0}, pos: "wr", tag: "19" },
// { placement: { relativeX: 67, relativeY: 0}, pos: "wr", tag: "19" },
// { placement: { relativeX: 83, relativeY: 0}, pos: "wr", tag: "19" }]
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

    componentDidMount() {

      }

    componentWillReceiveProps(nextProps) {
      console.log('Component : nextprops: ', nextProps)
      this.setState({placements: nextProps.placements.formation.playerMarkers})

      d3.select("#renderBox").html("");
      nextProps.placements.formation.playerMarkers.forEach( (x)=> {
             this.addPlayer(d3.select("#renderBox"), x)
      })
    }

    drawFieldGrid(x1,y1,x2,y2){
      var horizontalUnits = ((x2-x1)/10)
      var verticalUnits = ((y2-y1)/10)

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

          var i = 0;
          x.moveDescriptors.forEach((y) => {
            console.log(JSON.stringify(x));
            i++;

            switch( y.move ){
              case "curl":
                this.generateCurlRoute(g, player, coordinates, y)
                break;
              case "go":
                this.generateGoRoute(g, player, coordinates, y )
                break;
              case "custom":
                this.generateCustomRoute(g, player, coordinates, y, i==x.moveDescriptors.length);
                break;
              }

          })
                    })
        }
    }

    generateCustomRoute(g, player,coordinates,route, last ){
      console.log(JSON.stringify(route));
      console.log("Generate Custom Route" );
      var newStartPoints = this.convertX( route.start.relativeX, route.start.relativeY );
      var newEndPoints = this.convertX( route.end.relativeX, route.end.relativeY );

      var points = "";
      console.log( route.start.relativeX + " " + route.start.relativeY + " " + route.end.relativeX + " " + route.end.relativeY );

      g.append("polyline")
        .attr( "points", newStartPoints.x + " " + newStartPoints.y + " " + newEndPoints.x + " " + newEndPoints.y  )
        .attr("fill", "none" )
        .attr( "stroke-width", "5px" )
        .attr("stroke",routeColor)
        .attr("marker-end", last ? "url(#head)" : "")
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
        .attr( "fill", "skyblue" )
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

              <g id="renderBox">

              </g>
              </svg>
            </div>
        );
    }
}


const mapStateToProps = (state) =>{
  return {
    placements: state.active_card_id
  }
}
export default connect(
 mapStateToProps
)(Field);
