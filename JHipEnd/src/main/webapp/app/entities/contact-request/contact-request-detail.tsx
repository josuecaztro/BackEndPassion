import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contact-request.reducer';

export const ContactRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contactRequestEntity = useAppSelector(state => state.contactRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contactRequestDetailsHeading">
          <Translate contentKey="jhipsterApp.contactRequest.detail.title">ContactRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contactRequestEntity.id}</dd>
          <dt>
            <span id="messageBody">
              <Translate contentKey="jhipsterApp.contactRequest.messageBody">Message Body</Translate>
            </span>
          </dt>
          <dd>{contactRequestEntity.messageBody}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="jhipsterApp.contactRequest.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{contactRequestEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="jhipsterApp.contactRequest.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{contactRequestEntity.lastName}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="jhipsterApp.contactRequest.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{contactRequestEntity.phoneNumber}</dd>
        </dl>
        <Button tag={Link} to="/contact-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contact-request/${contactRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContactRequestDetail;
