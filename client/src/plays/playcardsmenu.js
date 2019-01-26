import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import DirectionsIcon from '@material-ui/icons/Directions';
import * as d3 from "d3";
import axios from 'axios';
import {connect} from 'react-redux';
import {handleNavigationClick} from '../reducers'

const styles = {
card: {
  minWidth: 275,
},
bullet: {
  display: 'inline-block',
  margin: '0 2px',
  transform: 'scale(0.8)',
},
title: {
  fontSize: 14,
},
pos: {
  marginBottom: 12,
},
};

var playCards = [];

class PersonList extends React.Component{
  // const { classes } = props;
  // const bull = <span className={classes.bullet}>•</span>;
state = {
    playCards: []
}

componentDidMount(){
  axios.get("http://server.immersivesports.ai/playcards")
    .then(res => {
      this.setState({playCards: res.data});
      if( res.data.length > 0 )
        this.props.navigate(res.data[0])
    })
}

render(){
  return (
    <div id="availableCards">
     { this.state.playCards.map(card =>
       <div>
         <Card>
          <CardContent>

          <Typography variant="h5" component="h2">
            {card.name}
          </Typography>
          <Typography  color="textSecondary">
            User Defined Formation
          </Typography>
          <Typography component="p">
            Imported
          </Typography>
          <Button onClick={()=>this.props.navigate(card)}>VIEW</Button>
          </CardContent>
         </Card>
         <br/>
       </div>
   )}
    </div>
  );
}
}

const mapDispatchToProps = dispatch => { return {dispatch, navigate: (card) => dispatch(handleNavigationClick(card))}}


// const mapDispatchToProps = dispatch;

export default connect(
null,
mapDispatchToProps
)(PersonList);

// PlayCardMenu.propTypes = {
//   classes: PropTypes.object.isRequired,
// };

// export default withStyles(styles)(PlayCardMenu);
