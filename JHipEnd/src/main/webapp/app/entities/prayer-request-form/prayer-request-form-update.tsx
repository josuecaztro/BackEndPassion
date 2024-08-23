import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrayerRequestForm } from 'app/shared/model/prayer-request-form.model';
import { getEntity, updateEntity, createEntity, reset } from './prayer-request-form.reducer';

export const PrayerRequestFormUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const prayerRequestFormEntity = useAppSelector(state => state.prayerRequestForm.entity);
  const loading = useAppSelector(state => state.prayerRequestForm.loading);
  const updating = useAppSelector(state => state.prayerRequestForm.updating);
  const updateSuccess = useAppSelector(state => state.prayerRequestForm.updateSuccess);

  const handleClose = () => {
    navigate('/prayer-request-form');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...prayerRequestFormEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...prayerRequestFormEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.prayerRequestForm.home.createOrEditLabel" data-cy="PrayerRequestFormCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.prayerRequestForm.home.createOrEditLabel">Create or edit a PrayerRequestForm</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="prayer-request-form-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterApp.prayerRequestForm.description')}
                id="prayer-request-form-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.prayerRequestForm.timeFrame')}
                id="prayer-request-form-timeFrame"
                name="timeFrame"
                data-cy="timeFrame"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.prayerRequestForm.firstName')}
                id="prayer-request-form-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.prayerRequestForm.lastName')}
                id="prayer-request-form-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.prayerRequestForm.phoneNumber')}
                id="prayer-request-form-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prayer-request-form" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PrayerRequestFormUpdate;
