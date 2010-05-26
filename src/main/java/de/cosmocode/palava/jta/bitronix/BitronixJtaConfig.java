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

import de.cosmocode.palava.jta.JtaConfig;

/**
 * Static constant holder class for bitronix config key names.
 * 
 * @author Tobias Sarnowski
 */
public final class BitronixJtaConfig {

    public static final String PREFIX = JtaConfig.PREFIX + "bitronix.";

    public static final String ENVIRONMENT = PREFIX + "environment";

    public static final String JOURNAL1 = PREFIX + "journal1";
    public static final String JOURNAL2 = PREFIX + "journal2";
    
    private BitronixJtaConfig() {
        
    }

}
