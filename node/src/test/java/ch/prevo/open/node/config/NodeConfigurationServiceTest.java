/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 *
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.node.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeConfigurationService.class})
public class NodeConfigurationServiceTest {

    @Inject
    private NodeConfigurationService nodeConfigService;

    @Test
    public void readConfigForOwnUIDs() {
        assertNotNull(nodeConfigService.getPrivateKey("CHE-109.537.519"));
        assertNotNull(nodeConfigService.getPublicKey("CHE-109.537.519"));
    }

    @Test
    public void readConfigForOtherUIDs() {
        assertNotNull(nodeConfigService.getPublicKey("CHE-109.740.084"));
        assertNotNull(nodeConfigService.getPublicKey("CHE-109.537.488"));
    }

    @Test
    public void attemptReadConfigForUnknownUID() {
        assertNull(nodeConfigService.getPublicKey("12345"));
    }

}
