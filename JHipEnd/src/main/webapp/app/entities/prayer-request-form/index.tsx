import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PrayerRequestForm from './prayer-request-form';
import PrayerRequestFormDetail from './prayer-request-form-detail';
import PrayerRequestFormUpdate from './prayer-request-form-update';
import PrayerRequestFormDeleteDialog from './prayer-request-form-delete-dialog';

const PrayerRequestFormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PrayerRequestForm />} />
    <Route path="new" element={<PrayerRequestFormUpdate />} />
    <Route path=":id">
      <Route index element={<PrayerRequestFormDetail />} />
      <Route path="edit" element={<PrayerRequestFormUpdate />} />
      <Route path="delete" element={<PrayerRequestFormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrayerRequestFormRoutes;
