import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/prayer-request-form">
        <Translate contentKey="global.menu.entities.prayerRequestForm" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contact-request">
        <Translate contentKey="global.menu.entities.contactRequest" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
