/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.jta.bitronix;

import java.io.File;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.Configuration;
import bitronix.tm.TransactionManagerServices;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.palava.core.inject.PalavaEnvironment;
import de.cosmocode.palava.core.lifecycle.Disposable;
import de.cosmocode.palava.core.lifecycle.Initializable;
import de.cosmocode.palava.core.lifecycle.LifecycleException;
import de.cosmocode.palava.jta.JtaProvider;

/**
 * Bitronix {@link JtaProvider} implementation.
 * 
 * @author Tobias Sarnowski
 */
class BitronixJtaProvider implements JtaProvider, Initializable, Disposable {
    
    private static final Logger LOG = LoggerFactory.getLogger(BitronixJtaProvider.class);

    private final File journal1;
    private final File journal2;

    private BitronixTransactionManager btm;
    private String environment;

    @Inject
    public BitronixJtaProvider(
        @PalavaEnvironment String environment,
        @Named(BitronixJtaConfig.JOURNAL1) File journal1,
        @Named(BitronixJtaConfig.JOURNAL2) File journal2) {
        this.environment = environment;
        this.journal1 = journal1.getAbsoluteFile();
        this.journal2 = journal2.getAbsoluteFile();
    }

    @Inject(optional = true)
    void setEnvironment(@Named(BitronixJtaConfig.ENVIRONMENT) String environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() throws LifecycleException {
        LOG.debug("Configuring Bitronix JTA provider...");
        final Configuration configuration = TransactionManagerServices.getConfiguration();

        LOG.debug("Bitronix ID: {}", environment);        
        configuration.setServerId(environment);

        LOG.debug("Journal 1: {}", journal1);
        configuration.setLogPart1Filename(journal1.toString());
        LOG.debug("Journal 2: {}", journal2);
        configuration.setLogPart2Filename(journal2.toString());

        configuration.setDisableJmx(false);

        btm = TransactionManagerServices.getTransactionManager();

        try {
            LOG.info("Starting Bitronix JTA provider as '{}'", configuration.getServerId());
            btm.begin();
        } catch (NotSupportedException e) {
            throw new LifecycleException(e);
        } catch (SystemException e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public TransactionManager getTransactionManager() {
        return btm;
    }

    @Override
    public UserTransaction getUserTransaction() {
        return btm;
    }

    @Override
    public void dispose() throws LifecycleException {
        btm.shutdown();
    }
    
}
