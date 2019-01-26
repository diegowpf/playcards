import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import LinearProgress from '@material-ui/core/LinearProgress';

const styles = {
  root: {
    flexGrow: 1,
  },
};

class Focus extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState({ value });
  };

  render() {
    const { classes } = this.props;

    return (
      <div>
      <Paper className={classes.root}>
        <Tabs
          value={this.state.value}
          onChange={this.handleChange}
          indicatorColor="primary"
          textColor="primary"
          centered
        >
          <Tab label="OFFENSE" />
          <Tab label="DEFENSE" disabled />
          <Tab label="SPECIAL TEAMS" disabled/>
        </Tabs>

      </Paper>
      <LinearProgress variant="determinate" value={0} />
      </div>
    );
  }
}

Focus.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Focus);
