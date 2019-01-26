import React from 'react'
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import { withStyles } from '@material-ui/core/styles';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {uploadFile} from '../reducers'
import axios from 'axios'
import compose from 'recompose/compose';

const styles = theme => ({
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
  button: {
  margin: theme.spacing.unit,
  },
  input: {
    display: 'none',
  },
});

class DocumentUploader extends React.Component {

  updateProgress(progress) {
    console.log("current progress", progress );
  }

  render(){

    const {classes} = this.props;
    console.log( "this is the props", this.props);
    console.log("this is the upload", (this.props.upload));

    return ( <Fab color="primary" aria-label="Add">
      <input
       accept="application/vnd.openxmlformats-officedocument.presentationml.slideshow,application/vnd.openxmlformats-officedocument.presentationml.presentation"
       id="contained-button-file"
       className={classes.input}
       onChange={this.props.upload}
       multiple
       type="file"
     />

     <label htmlFor="contained-button-file">
      <AddIcon />
     </label>
    </Fab>);
  }
}

DocumentUploader.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapDispatchToProps = dispatch => {
  return {dispatch, upload: (file) => dispatch(uploadFile(file),)
  }
}

console.log("dispatch props", mapDispatchToProps );
// export default connect(null, mapDispatchToProps)(withStyles(styles)(DocumentUploader));
// export default withStyles(styles)(connect(null, mapDispatchToProps)(DocumentUploader));

export default compose(
  withStyles(styles, { name: 'DocumentUploader' }),
  connect(null, mapDispatchToProps)
)(DocumentUploader);
