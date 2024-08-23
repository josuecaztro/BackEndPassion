import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './prayer-request-form.reducer';

export const PrayerRequestFormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const prayerRequestFormEntity = useAppSelector(state => state.prayerRequestForm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="prayerRequestFormDetailsHeading">
          <Translate contentKey="jhipsterApp.prayerRequestForm.detail.title">PrayerRequestForm</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="jhipsterApp.prayerRequestForm.description">Description</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.description}</dd>
          <dt>
            <span id="timeFrame">
              <Translate contentKey="jhipsterApp.prayerRequestForm.timeFrame">Time Frame</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.timeFrame}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="jhipsterApp.prayerRequestForm.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="jhipsterApp.prayerRequestForm.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.lastName}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="jhipsterApp.prayerRequestForm.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{prayerRequestFormEntity.phoneNumber}</dd>
        </dl>
        <Button tag={Link} to="/prayer-request-form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prayer-request-form/${prayerRequestFormEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrayerRequestFormDetail;
